package ece351.vhdl.test;

import java.io.File;
import java.util.Collection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import ece351.util.BaseTest351;
import ece351.util.CommandLine;
import ece351.util.TestInputs351;
import ece351.vhdl.VRecognizer;

@RunWith(Parameterized.class)
public class TestVRecognizerAccept extends BaseTest351 {

	private final File f;

	public TestVRecognizerAccept(final File f) {
		this.f = f;
	}

	@Parameterized.Parameters
	public static Collection<Object[]> files() {
		return TestInputs351.vhdlFiles();
	}

	@Test
	public void accept() {
		final String inputSpec = f.getAbsolutePath();
		System.out.println("reading: " + inputSpec);
		final CommandLine c = new CommandLine(inputSpec);
		System.out.println(c.readInputSpec());
		VRecognizer.main(inputSpec);
		System.out.println("accepted, as expected:  " + inputSpec);
	}


}
