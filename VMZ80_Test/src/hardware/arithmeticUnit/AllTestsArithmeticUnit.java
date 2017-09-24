package hardware.arithmeticUnit;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ AllRotatesArithmeticUnit.class,
	AllStandardTestsArithmeticUnit.class,
	BitsArithmeticUnit.class,
	DAATestArithmeticUnit.class})
public class AllTestsArithmeticUnit {

}
