package hardware;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ hardware.AllTestsMisc.class,
				hardware.arithmeticUnit.AllTests.class,
				hardware.centralProcessingUnit.AllTests.class,
				hardware.instruction.AllTests.class,
				
	})
public class AllTests {

}
