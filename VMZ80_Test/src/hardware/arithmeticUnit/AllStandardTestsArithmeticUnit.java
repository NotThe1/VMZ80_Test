package hardware.arithmeticUnit;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ ByteAddArithmeticUnit.class,
	ByteSubtractArithmeticUnit.class,
	WordAddArithmeticUnit.class,
	WordSubtractArithmeticUnit.class,
	LogicalOperationsArithmeticUnit.class })
public class AllStandardTestsArithmeticUnit {

}
