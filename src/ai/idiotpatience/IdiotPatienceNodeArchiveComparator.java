package ai.idiotpatience;

import java.util.Comparator;

public class IdiotPatienceNodeArchiveComparator implements Comparator<byte[]> {

	@Override
	public int compare (byte[] byteSetOne, byte[] byteSetTwo) {
	
		if (byteSetOne.length == byteSetTwo.length /*&& byteSetOne.length == 68*/) {
			for (int i = 0; i < byteSetOne.length; i++) {
				int diff = byteSetOne[i] - byteSetTwo[i];
				if (diff == 0) continue;
				return diff;
			}
			return 0;
		}
		return 1;
	}

}
