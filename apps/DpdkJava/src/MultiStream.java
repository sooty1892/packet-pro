import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;

public class MultiStream extends OutputStream {
	
	private OutputStream ps1;
	private OutputStream ps2;
	
	public MultiStream(OutputStream ps1, OutputStream ps2) {
		this.ps1 = ps1;
		this.ps2 = ps2;
	}

	@Override
	public void write(int b) throws IOException {
		ps1.write(b);
		ps2.write(b);
	}

}