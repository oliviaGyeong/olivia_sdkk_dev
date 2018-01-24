package device.sdk.ifmmanager;

/**
 * Created by mskl on 2018-01-23.
 */

public class splitPacket {
	public String	strKey;
	public String	strData;
	public int		sStart;
	public int		sEnd;
	
	splitPacket(int	sStart, int sEnd) {
		this.sStart=sStart;
		this.sEnd=sEnd;
	}
}
