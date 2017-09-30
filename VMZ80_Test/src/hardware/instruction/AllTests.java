package hardware.instruction;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ InstructionCB.class,
	            InstructionED.class,
	            InstructionDDandFD.class,
	            InstructionMain.class })
public class AllTests {

}
