package ece351.vhdl.ast;

import ece351.util.Examinable;
import ece351.util.Examiner;


public final class DesignUnit implements Examinable {
	public final Architecture arch;
	public final Entity entity;
	public final String identifier;

	//An Entity and an Architecture make up a Design Unit
	public DesignUnit(final Architecture arch, final Entity entity) {
		this.arch = arch;
		this.entity = entity;
		this.identifier = entity.identifier;
	}

	public DesignUnit setArchitecture(final Architecture arch2) {
		return new DesignUnit(arch2, entity);
	}

	public DesignUnit setEntity(final Entity entity2) {
		return new DesignUnit(arch, entity2);
	}
	
	@Override
	public String toString() {
		return this.entity + "\n" + this.arch + "\n";
	}

	@Override
	public int hashCode() {
		return identifier.hashCode();
	}
	
	@Override
	public boolean equals(final Object obj) {
		if (!(obj instanceof Examinable)) return false;
		return examine(Examiner.Equals, (Examinable)obj);
	}

	@Override
	public boolean isomorphic(final Examinable obj) {
		return examine(Examiner.Isomorphic, obj);
	}

	@Override
	public boolean equivalent(final Examinable obj) {
		return examine(Examiner.Equivalent, obj);
	}

	private boolean examine(final Examiner examiner, final Examinable obj) {
		// basics
		if (obj == null) return false;
		if (!obj.getClass().equals(this.getClass())) return false;
		final DesignUnit that = (DesignUnit) obj;

		assert this.repOk();
		assert that.repOk();
		
		// compare field values
		if (!this.identifier.equals(that.identifier) 
				|| !examiner.examine(this.arch, that.arch)
				|| !examiner.examine(this.entity, that.entity)) return false;

		// no significant differences
		return true;
	}

	public boolean repOk() {
		assert arch != null;
		assert entity != null;
		assert identifier != null;
		assert identifier.equals(entity.identifier);
		assert arch.repOk();
		assert entity.repOk();
		return true;
	}

}
