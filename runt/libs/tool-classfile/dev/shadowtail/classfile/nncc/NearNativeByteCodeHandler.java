// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package dev.shadowtail.classfile.nncc;

import dev.shadowtail.classfile.xlate.ByteCodeHandler;
import dev.shadowtail.classfile.xlate.ByteCodeState;
import dev.shadowtail.classfile.xlate.ExceptionClassEnqueueAndTable;
import dev.shadowtail.classfile.xlate.ExceptionHandlerRanges;
import dev.shadowtail.classfile.xlate.ExceptionStackAndTable;
import dev.shadowtail.classfile.xlate.InvokeType;
import dev.shadowtail.classfile.xlate.JavaStackEnqueueList;
import dev.shadowtail.classfile.xlate.JavaStackPoison;
import dev.shadowtail.classfile.xlate.JavaStackResult;
import dev.shadowtail.classfile.xlate.JavaStackState;
import dev.shadowtail.classfile.xlate.MathType;
import dev.shadowtail.classfile.xlate.StackJavaType;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import net.multiphasicapps.classfile.ByteCode;
import net.multiphasicapps.classfile.ClassName;
import net.multiphasicapps.classfile.MethodReference;

/**
 * This contains the handler for the near native byte code.
 *
 * @since 2019/04/06
 */
public final class NearNativeByteCodeHandler
	implements ByteCodeHandler
{
	/** State of the byte code. */
	public final ByteCodeState state =
		new ByteCodeState();
	
	/** Used to build native code. */
	protected final NativeCodeBuilder codebuilder =
		new NativeCodeBuilder();
	
	/** Exception tracker. */
	protected final ExceptionHandlerRanges exceptionranges;
	
	/** Standard exception handler table. */
	private final Map<ExceptionStackAndTable, __EData__> _ehtable =
		new LinkedHashMap<>();
	
	/** Made exception table. */
	private final Map<ClassStackAndLabel, __EData__> _metable =
		new LinkedHashMap<>();
	
	/** The returns which have been performed. */
	private final List<JavaStackEnqueueList> _returns =
		new ArrayList<>();
	
	/** Last registers enqueued. */
	private JavaStackEnqueueList _lastenqueue;
	
	/**
	 * Initializes the byte code handler.
	 *
	 * @param __bc The byte code.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/04/11
	 */
	public NearNativeByteCodeHandler(ByteCode __bc)
		throws NullPointerException
	{
		if (__bc == null)
			throw new NullPointerException("NARG");
		
		this.exceptionranges = new ExceptionHandlerRanges(__bc);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/04/07
	 */
	@Override
	public final void doCopy(JavaStackResult.Input __in,
		JavaStackResult.Output __out)
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/04/10
	 */
	@Override
	public final void doInvoke(InvokeType __t, MethodReference __r,
		JavaStackResult.Output __out, JavaStackResult.Input... __in)
	{
		NativeCodeBuilder codebuilder = this.codebuilder;
		
		// Push references
		this.__refPush();
		
		// Checks on the instance
		if (__t.hasInstance())
		{
			// The instance register
			int ireg = __in[0].register;
			
			// Cannot be null
			codebuilder.addIfZero(ireg, this.__labelMakeException(
				"java/lang/NullPointerException"), true);
			
			// Must be the given class
			codebuilder.addIfNotClass(__r.handle().outerClass(), ireg,
				this.__labelMakeException("java/lang/ClassCastException"),
				true);
		}
		
		// Fill in call arguments
		List<Integer> callargs = new ArrayList<>(__in.length * 2);
		for (int i = 0, n = __r.handle().javaStack(__t.hasInstance()).length;
			i < n; i++)
		{
			// Add the input register
			JavaStackResult.Input in = __in[i];
			callargs.add(in.register);
			
			// But also if it is wide, we need to pass the other one or else
			// the value will be clipped
			if (in.type.isWide())
				callargs.add(in.register + 1);
		}
		
		// Add invocation
		codebuilder.add(NativeInstructionType.INVOKE,
			new InvokedMethod(__t, __r.handle()), new RegisterList(callargs));
		
		// Read in return value
		if (__out != null)
			throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/04/07
	 */
	@Override
	public final void doMath(StackJavaType __dt, MathType __mt,
		JavaStackResult.Input __a, JavaStackResult.Input __b,
		JavaStackResult.Output __c)
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/04/07
	 */
	@Override
	public final void doMath(StackJavaType __dt, MathType __mt,
		JavaStackResult.Input __a, Number __b, JavaStackResult.Output __c)
	{
		this.codebuilder.addMathConst(__dt, __mt, __a.register, __b,
			__c.register);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/04/11
	 */
	@Override
	public final void doReturn(JavaStackResult.Input __in)
	{
		NativeCodeBuilder codebuilder = this.codebuilder;
		
		// Returning a value?
		if (__in != null)
		{
			throw new todo.TODO();
		}
		
		// Do the return
		this.__generateReturn();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/04/07
	 */
	@Override
	public final void instructionFinish()
	{
		ByteCodeState state = this.state;
		
		// An exception check was requested, do a check on the exception
		// register and jump if there is something there
		if (state.canexception)
			codebuilder.addIfNonZero(NativeCode.EXCEPTION_REGISTER,
				this.__labelException(), true);
	}
	
	/**
	 * Sets up before processing the instruction.
	 *
	 * @since 2019/04/07
	 */
	public final void instructionSetup()
	{
		NativeCodeBuilder codebuilder = this.codebuilder;
		ByteCodeState state = this.state;
		int addr = state.addr;
		
		// Check if we need to transition into this instruction from the
		// previous natural execution point (not a result of a jump)
		JavaStackPoison poison = state.stackpoison.get(addr);
		if (poison != null)
		{
			throw new todo.TODO();
		}
		
		// Setup a label for this current position, this is done after
		// potential flushing because it is assumed that the current state
		// is always valid even after a flush
		codebuilder.label("java", addr);
	}
	
	/**
	 * Returns the result of the translation.
	 *
	 * @return The translation result.
	 * @since 2019/04/07
	 */
	public final NativeCode result()
	{
		// Generate make exception code
		Map<ClassStackAndLabel, __EData__> metable = this._metable;
		if (!metable.isEmpty())
		{
			throw new todo.TODO();
		}
		
		// Generate exception handler tables
		Map<ExceptionStackAndTable, __EData__> ehtable = this._ehtable;
		if (!ehtable.isEmpty())
		{
			throw new todo.TODO();
		}
		
		return this.codebuilder.build();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/04/06
	 */
	@Override
	public final ByteCodeState state()
	{
		return this.state;
	}
	
	/**
	 * Generates or jumps to another return point.
	 *
	 * @return The label to this return point.
	 * @since 2019/04/11
	 */
	private final NativeCodeLabel __generateReturn()
	{
		return this.__generateReturn(this.state.stack.possibleEnqueue());
	}
	
	/**
	 * Generates or jumps to another return point for the given enqueue.
	 *
	 * @param __eq The enqueue to return for.
	 * @return The label to this return point.
	 * @throws NullPointerException On null arguments.
	 * @return
	 */
	private final NativeCodeLabel __generateReturn(JavaStackEnqueueList __eq)
		throws NullPointerException
	{
		if (__eq == null)
			throw new NullPointerException("NARG");
		
		// Find unique return point
		boolean freshdx;
		List<JavaStackEnqueueList> returns = this._returns;
		int dx = returns.indexOf(__eq);
		if ((freshdx = (dx < 0)))
			returns.add((dx = returns.size()), __eq);
		
		// Label used for return
		NativeCodeLabel lb = new NativeCodeLabel("return", dx);
		
		// If this was never added here, make sure a label exists
		if (freshdx)
			codebuilder.label(lb);
		
		// If the enqueue list is empty then the only thing we need to do
		// is generate a return instruction
		NativeCodeBuilder codebuilder = this.codebuilder;
		if (__eq.isEmpty())
		{
			// Since there is nothing to uncount, just return
			codebuilder.add(NativeInstructionType.RETURN);
			
			return lb;
		}
		
		// If we are not making a fresh index there more things to clear out
		// then just jump to the pre-existing return point
		if (!freshdx && __eq.size() > 1)
		{
			// Jump to label
			codebuilder.addGoto(lb);
			
			return lb;
		}
		
		// Since the enqueue list is not empty, we can just trim a register
		// from the top and recursively go down
		// So uncount the top
		codebuilder.add(NativeInstructionType.UNCOUNT, __eq.top());
		
		// Recursively go down since the enqueues may possibly be shared, if
		// any of these enqueues were previously made then the recursive
		// call will just make a goto
		this.__generateReturn(__eq.trimTop());
		
		// Note that we do not return the recursive result because that
		// will be for another enqueue state
		return lb;
	}
	
	/**
	 * Creates and stores an exception.
	 *
	 * @return The label to the exception.
	 * @since 2019/04/09
	 */
	private final NativeCodeLabel __labelException()
	{
		// Setup key
		ByteCodeState state = this.state;
		ExceptionStackAndTable key = this.exceptionranges.stackAndTable(
			state.stack, state.addr);
		
		// Try to use an already existing point
		Map<ExceptionStackAndTable, __EData__> ehtable = this._ehtable;
		__EData__ rv = ehtable.get(key);
		if (rv != null)
			return rv.label;
			
		// Build new data to record this point
		rv = new __EData__(state.addr, state.line,
			new NativeCodeLabel("exception", ehtable.size()));
		ehtable.put(key, rv);
		
		// Return the created label (where the caller jumps to)
		return rv.label;
	}
	
	/**
	 * Makes a label which creates the given exception then throws that
	 * exception.
	 *
	 * @param __cl The class to create.
	 * @return The label for that target.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/04/10
	 */
	private final NativeCodeLabel __labelMakeException(String __cl)
		throws NullPointerException
	{
		if (__cl == null)
			throw new NullPointerException("NARG");
		
		// Setup key, the label is the target to jump to after the exception
		// has been generated and a throw is performed
		ByteCodeState state = this.state;
		ClassStackAndLabel key = new ClassStackAndLabel(new ClassName(__cl),
			state.stack, this.__labelException());
		
		// Look in the table to see if we made it before
		Map<ClassStackAndLabel, __EData__> metable = this._metable;
		__EData__ rv = metable.get(key);
		if (rv != null)
			return rv.label;
		
		// Build new data to record this point
		rv = new __EData__(state.addr, state.line,
			new NativeCodeLabel("makeexception", metable.size()));
		metable.put(key, rv);
		
		// Return the created label (where the caller jumps to)
		return rv.label;
	}
	
	/**
	 * If anything has been previously pushed then generate code to clear it.
	 *
	 * @since 2019/03/30
	 */
	private final void __refClear()
	{
		// Do nothing if nothing has been enqueued
		JavaStackEnqueueList lastenqueue = this._lastenqueue;
		if (lastenqueue == null)
			return;
		
		// Generate instruction to clear the enqueue
		this.codebuilder.add(NativeInstructionType.REF_CLEAR);
		
		// No need to clear anymore
		this._lastenqueue = null;
	}
	
	/**
	 * Generates code to enqueue registers, if there are any. This implicitly
	 * uses the registers from the state.
	 *
	 * @return True if the push list was not empty.
	 * @since 2019/04/10
	 */
	private final boolean __refPush()
		throws NullPointerException
	{
		return this.__refPush(this.state.result.enqueue());
	}
	
	/**
	 * Generates code to enqueue registers, if there are any.
	 *
	 * @param __r The registers to push.
	 * @return True if the push list was not empty.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/03/30
	 */
	private final boolean __refPush(JavaStackEnqueueList __r)
		throws NullPointerException
	{
		if (__r == null)
			throw new NullPointerException("NARG");
		
		// Nothing to enqueue?
		if (__r.isEmpty())
		{
			this._lastenqueue = null;
			return false;
		}
		
		// Generate code to push all the given registers
		this.codebuilder.add(NativeInstructionType.REF_PUSH,
			new RegisterList(__r.registers()));
		this._lastenqueue = __r;
		
		// Did enqueue something
		return true;
	}
}

