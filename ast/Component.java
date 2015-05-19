package ece351.vhdl.ast;

import java.util.TreeSet;

import org.parboiled.common.ImmutableList;

import ece351.util.Examinable;
import ece351.util.Examiner;
import ece351.util.Utils351;

public final class Component implements Examinable {
	public final String entityName;
	public final String instanceName;
	public final ImmutableList<String> signalList;
	public Component(
			final String entityName , 
			final String instanceName) {
		this.entityName = entityName;
		this.instanceName = instanceName;
		this.signalList = ImmutableList.of();
	}
	public Component(
			final ImmutableList<String> signals,
			final String entityName , 
			final String instanceName) {
		this.entityName = entityName;
		this.instanceName = instanceName;
		this.signalList = signals;
	}
	
	public Component appendSignal(final String signal) {
		return new Component(signalList.append(signal), entityName, instanceName);
	}
	
	public Component varyEntityName(final String name) {
		return new Component(signalList, name, instanceName);
	}
	
	public Component varyInstanceName(final String name) {
		return new Component(signalList, entityName, name);
	}
	
	public Component varySignals(final ImmutableList<String> list) {
		return new Component(list, entityName, instanceName);
	}
	
    @Override
    public String toString() {
        return instanceName + " : entity work." + entityName + " port map( " + Utils351.bitListToString(signalList) + ");";
    }

	@Override
	public int hashCode() {
		return entityName.hashCode();
	}
	
    @Override
	public boolean equals(final Object obj) {
		// basics
		if (obj == null) return false;
		if (!obj.getClass().equals(this.getClass())) return false;
		final Component that = (Component) obj;
		
		// compare field values using Examiner.orderedEquals()
		if (!this.entityName.equals(that.entityName)
			|| !this.instanceName.equals(that.instanceName)
			|| !Examiner.orderedEquals(this.signalList, that.signalList)) return false;
		
		// no significant differences
		return true;
	}
	
	@Override
	public boolean isomorphic(final Examinable obj) {		
		// basics
		if (obj == null) return false;
		if (!obj.getClass().equals(this.getClass())) return false;
		final Component that = (Component) obj;
		
		// compare field values using Examiner.unorderedEquals()
		if (!this.entityName.equals(that.entityName)
			|| !this.instanceName.equals(that.instanceName)
			|| !Examiner.unorderedEquals(this.signalList, that.signalList)) return false;
		
		// no significant differences
		return true;
	}

	@Override
	public boolean equivalent(final Examinable obj) {
		return isomorphic(obj);
	}
	
	public boolean repOk() {
		assert entityName != null;
		assert instanceName != null;
		assert signalList != null;
		assert entityName.length() > 0;
		assert instanceName.length() > 0;
		// check there are no duplicates in the signal list
		final TreeSet<String> s = new TreeSet<String>(signalList);
		assert s.size() == signalList.size();
		return true;
	}

}
