import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.function.Consumer;

public class CSVReader implements Iterable<String[]> {
    private final ArrayList<String[]> lines;
    private class CSVIterator implements Iterator<String[]> {
        private int idx = 0;

        @Override
        public boolean hasNext() {
            return idx < lines.size();
        }

        @Override
        public String[] next() {
            return lines.get(idx++);
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }

        @Override
        public void forEachRemaining(Consumer consumer) {
            throw new UnsupportedOperationException();
        }
    }
    public CSVReader() {
        lines = new ArrayList<String[]>();
    }

    @Override
    public Iterator<String[]> iterator() {
        return new CSVIterator();
    }

    /**
     * @param path 파일경로
     * @throws IOException 경로에 파일이 없는 경우
     */
    public void read(String path) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(path));
        while (reader.ready()) {
            String line = reader.readLine();
            lines.add(line.split(","));
        }
    }

    /**
     * @return 첫번째 요소
     */
    public String[] getLine() {
        return lines.remove(0);
    }

    /**
     * @return 전체 요소
     */
    public String[][] getLines() {
        String[][] lines2D = new String[lines.size()][];
        for(int i=0; i < lines.size(); i++) {
            lines2D[i] = lines.get(i);
        }
        return lines2D;
    }
}
