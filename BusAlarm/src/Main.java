import java.io.IOException;


public class Main {

	//명지대역 셔틀 버스 데이터
	private static StationData MJStaion;
	//기흥역 셔틀 버스 데이터
	private static StationData KHStation;
	//시내 셔틀 버스 데이터
	private static StationData SINEStaion;

	public static void main(String[] args){

		try {
			SINEStaion = new StationData(".//csv//시내.csv");
			KHStation = new StationData(".//csv//기흥역.csv");
		} catch (IOException e) {
			e.printStackTrace();
		}
		GetBusTime getBusTime = new GetBusTime();

		//GetBusTime getKHSBusTime = new GetBusTime(KHStation);

		DateFormat time = new DateFormat("16:00");

		//16:35
		System.out.println(	getBusTime.getTargetBusTime(SINEStaion, time).getTime());
		//16:35 + 19분 = 16:54분 도착 예정
		System.out.println(getBusTime.getArrivalTime(SINEStaion,time, "이마트").getTime());

		//16:10
		System.out.println(getBusTime.getTargetBusTime(KHStation, time).getTime());
		//16:10 + 8분 = 16:18분 도착예정
		System.out.println(getBusTime.getArrivalTime(KHStation, time,"진입로").getTime());
	}
}
