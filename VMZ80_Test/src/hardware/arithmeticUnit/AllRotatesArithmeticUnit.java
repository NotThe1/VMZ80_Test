package hardware.arithmeticUnit;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ RotateLeftArithmeticUnit.class,
	ShiftArithmeticUnit.class,
	RotateRightArithmeticUnit.class })
public class AllRotatesArithmeticUnit {

}
