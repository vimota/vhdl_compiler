package ece351.vhdl.ast;
import org.parboiled.common.ImmutableList;

import ece351.util.Examinable;
import ece351.util.Examiner;

public final class VProgram implements Examinable {
	//may not need to make AST for these nodes because we do not need to do 
	//anything with it, we COULD check if they are using things that are defined in the libraries
	//but that is too much work
	
	public final ImmutableList<DesignUnit> designUnits;
	
	/**
	 * Constructs a VProgram with an empty list of design units
	 */
	public VProgram() {
		this.designUnits = ImmutableList.of();
	}
	
	/**
	 * Constucts a VProgram with a list of design units given by parameter designUnits
	 * @param designUnits
	 */
	public VProgram(final ImmutableList<DesignUnit> designUnits) {
		this.designUnits = designUnits;
	}

	public boolean repOk() {
		assert designUnits != null;
		for (final DesignUnit du : designUnits) {
			assert du.repOk();
		}
		return true;
	}

	/**
	 * Construct a new VProgram with this VProgram's design unit list plus d.
	 * @param d
	 * @return new VProgram with parameter d appended to the original list
	 */
	public VProgram append(final DesignUnit d) {
		return new VProgram(designUnits.append(d));
	}
	
	public VProgram setDesignUnits(final ImmutableList<DesignUnit> list) {
		return new VProgram(list);
	}

    @Override
    public String toString() {
        final StringBuilder b = new StringBuilder();
        for (final DesignUnit d : designUnits) {                 
                b.append(d.toString());
        }
        return b.toString();
    }

	@Override
	public int hashCode() {
		return 42;
	}
	
    @Override
	public boolean equals(final Object obj) {
		// basics
		if (obj == null) return false;
		if (!obj.getClass().equals(this.getClass())) return false;
		final VProgram that = (VProgram) obj;
		
		// compare field values using Examiner.orderedExamination()
		if (!Examiner.orderedExamination(Examiner.Equals, this.designUnits, that.designUnits)) return false;
		
		// no significant differences
		return true;
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
		final VProgram that = (VProgram) obj;
		
		// compare field values using Examiner.unorderedExamination()
		if (!Examiner.unorderedExamination(examiner, this.designUnits, that.designUnits)) return false;
		
		// no significant differences
		return true;
	}

}
