package ece351.vhdl;

import org.parboiled.Rule;
import org.parboiled.annotations.MemoMismatches;

import ece351.util.BaseParser351;

abstract class VBase extends BaseParser351 implements VConstants {

	Rule Char() {
		return FirstOf(CharRange('a', 'z'), CharRange('A', 'Z'));
	}

	Rule Digit() {
		return CharRange('0', '9');
	}

	// *************************************Keyword
	// Rules******************************************
	// You can use these rules to match keywords in the VHDL grammar rules.
	// Theses rules account
	// for VHDL's case-insensitivity and enforce whitespaces following the
	// keywords more carefully
	// than just using the rule, WhiteSpace, or adding a space at the end of
	// each keyword literal

	// Boolean Operators
	public Rule SingleKeyword(String keyword) {
		return Sequence(IgnoreCase(keyword),
				TestNot(FirstOf(Char(), Digit(), "_")));
	}

	Rule NOT() {
		return SingleKeyword(NOT);
	}

	Rule AND() {
		return SingleKeyword(AND);
	}

	Rule OR() {
		return SingleKeyword(OR);
	}

	Rule XOR() {
		return SingleKeyword(XOR);
	}

	Rule NAND() {
		return SingleKeyword(NAND);
	}

	Rule NOR() {
		return SingleKeyword(NOR);
	}

	Rule XNOR() {
		return SingleKeyword(XNOR);
	}

	// Constructs
	Rule IF() {
		return SingleKeyword("if");
	}

	Rule THEN() {
		return SingleKeyword("then");
	}

	Rule ELSE() {
		return SingleKeyword("else");
	}

	Rule ENDIF() {
		return Sequence(END(), W0(), IF());
	}

	Rule PROCESS() {
		return SingleKeyword("process");
	}

	Rule ENDPROCESS() {
		return Sequence(END(), W0(), PROCESS());
	}

	Rule ENTITY() {
		return SingleKeyword("entity");
	}

	Rule PORT() {
		return SingleKeyword("port");
	}

	Rule MAP() {
		return SingleKeyword("map");
	}

	Rule ARCHITECTURE() {
		return SingleKeyword("architecture");
	}

	Rule OF() {
		return SingleKeyword("of");
	}

	Rule IS() {
		return SingleKeyword("is");
	}

	Rule BEGIN() {
		return SingleKeyword("begin");
	}

	Rule END() {
		return SingleKeyword("end");
	}

	Rule SIGNAL() {
		return SingleKeyword("signal");
	}

	Rule BIT() {
		return SingleKeyword("bit");
	}

	Rule IN() {
		return SingleKeyword("in");
	}

	Rule OUT() {
		return SingleKeyword("out");
	}

	@MemoMismatches
	Rule Keyword() {
		return FirstOf(NOT(), AND(), OR(), XOR(), NAND(), NOR(), XNOR(), IF(),
				THEN(), ELSE(), PROCESS(), ENTITY(), PORT(), MAP(),
				ARCHITECTURE(), OF(), IS(), BEGIN(), END(), SIGNAL(), BIT(),
				IN(), OUT());
	}

}
