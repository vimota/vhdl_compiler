package ece351.vhdl.ast;

import org.parboiled.common.ImmutableList;

import ece351.util.Examinable;
import ece351.util.Examiner;
import ece351.util.Utils351;

public final class Entity implements Examinable {
	public final String identifier;
	public final ImmutableList<String> input;
	public final ImmutableList<String> output;
	
	public Entity(final String id) {
		this.identifier = id;
		this.input = ImmutableList.of();
		this.output = ImmutableList.of();
	}
	
	public Entity(final ImmutableList<String> out,
			final ImmutableList<String> in, final String id) {
		this.identifier = id;
		this.input = in;
		this.output = out;
	}
	
	public boolean repOk() {
		assert identifier != null;
		assert input != null;
		assert output != null;
		return true;
	}

	public Entity appendInput(final String i) {
		return new Entity(output, input.append(i), identifier);
	}
	
	public Entity appendOutput(final String o) {
		return new Entity(output.append(o), input, identifier);
	}
	
	public Entity setInput(final ImmutableList<String> list) {
		return new Entity(output, list, identifier);
	}
	
	public Entity setOutput(final ImmutableList<String> list) {
		return new Entity(list, input, identifier);
	}
	
    @Override
    public String toString() {
    	final String inputBits = Utils351.bitListToString(input);
    	final String outputBits = Utils351.bitListToString(output);
        return "entity " + identifier + " is port(\n" 
                + ((inputBits.length() > 0) ? "    " + inputBits + " : in bit;\n" : "")
                + ((outputBits.length() > 0) ? "    " + outputBits + " : out bit\n" : "")
                + ");\nend " + identifier + ";";
    }

	@Override
	public int hashCode() {
		return identifier.hashCode();
	}
	
    @Override
	public boolean equals(final Object obj) {
		// basics
		if (obj == null) return false;
		if (!obj.getClass().equals(this.getClass())) return false;
		final Entity that = (Entity) obj;
		
		// compare field values using Examiner.orderedEquals()
		if (!this.identifier.equals(that.identifier)
			|| !Examiner.orderedEquals(this.input, that.input)
			|| !Examiner.orderedEquals(this.output, that.output)) return false;
		
		// no significant differences
		return true;
	}
	
	@Override
	public boolean isomorphic(final Examinable obj) {
		return equals(obj);
	}

	@Override
	public boolean equivalent(final Examinable obj) {
		return isomorphic(obj);
	}

}
