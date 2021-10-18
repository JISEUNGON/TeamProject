import java.io.IOException;

public class StationData {

    private String[] stationNames;
    private int[] stationTimes;
    private DateFormat[] startTimes;
    private CSVReader reader;

    public StationData(String path) throws IOException {
        
        //데이터를 읽어들이고, 각각의 항목에 저장한다.
        reader = new CSVReader();

        reader.read(path);
        String [] line = reader.getLine();

        startTimes = new DateFormat[line.length-1];
        for(int i =1; i<line.length; i++){
            startTimes[i-1] = new DateFormat(line[i]);
        }

        line = reader.getLine();
        stationNames = new String[line.length-1];
        for(int i =1; i<line.length; i++){
            stationNames[i-1] = line[i];
        }

        line = reader.getLine();
        stationTimes = new int[line.length-1];
        for(int i =1; i<line.length; i++){
            stationTimes[i-1] = Integer.parseInt(line[i]);
        }
    }

    public String[] getStationNames() {
        return stationNames;
    }

    public int[] getStationTimes() {
        return stationTimes;
    }

    public DateFormat[] getStartTimes() {
        return startTimes;
    }
}
