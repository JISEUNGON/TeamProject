
public class Search {
	int time_;
	int[] time_list;
	String time_format;
	
	public void searchTime(String time, String[] list) {//이진 탐색 함수
		DateFormat format_ = new DateFormat(time);
		
		time_ = format_.getTotalMin();
		
		convertTime(list);
		
	}
	
	public void convertTime(String[] list) {//시간 리스트를 분으로 바꾸는 함수
		DateFormat format_;
		
		for(int i = 0; i < list.length; i++) {
			format_ = new DateFormat(list[i]);
			
			time_list[i] = format_.getTotalMin();
		}
	}
	
	public void compareTime() {
		int difference = Math.abs(time_ - time_list[0]);
		
		for(int i = 0; i < time_list.length; i++) {
			if(difference < Math.abs(time_ - time_list[i])) {
				time_format = (time_list[i - 1] / 60) + ":" + (time_list[i - 1] % 60);
				break;				
			}			
		}
	}
}
