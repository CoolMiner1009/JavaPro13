import java.util.*;

class FileData {
    private String name;
    private long size;
    private String path;

    public FileData(String name, long size, String path) {
        this.name = name;
        this.size = size;
        this.path = path;
    }

    public String getName() {
        return name;
    }

    public long getSize() {
        return size;
    }

    public String getPath() {
        return path;
    }
}

class FileNavigator {
    private Map<String, List<FileData>> files;

    public FileNavigator() {
        files = new HashMap<>();
    }

    public void add(FileData file) {
        String path = file.getPath();
        List<FileData> fileList = files.getOrDefault(path, new ArrayList<>());
        fileList.add(file);
        files.put(path, fileList);
    }

    public List<FileData> find(String path) {
        return files.getOrDefault(path, new ArrayList<>());
    }

    public List<FileData> filterBySize(String path, long maxSize) {
        List<FileData> fileList = files.getOrDefault(path, new ArrayList<>());
        List<FileData> filteredList = new ArrayList<>();
        for (FileData file : fileList) {
            if (file.getSize() <= maxSize) {
                filteredList.add(file);
            }
        }
        return filteredList;
    }

    public void remove(String path) {
        files.remove(path);
    }

    public List<FileData> sortBySize() {
        List<FileData> allFiles = new ArrayList<>();
        for (List<FileData> fileList : files.values()) {
            allFiles.addAll(fileList);
        }
        allFiles.sort(Comparator.comparingLong(FileData::getSize));
        return allFiles;
    }

    public void addWithConsistencyCheck(FileData file) {
        String path = file.getPath();
        String key = getKeyFromPath(path);
        List<FileData> fileList = files.getOrDefault(key, new ArrayList<>());
        if (fileList.isEmpty() || fileList.get(0).getPath().equals(path)) {
            fileList.add(file);
            files.put(key, fileList);
        } else {
            System.out.println("Inconsistent file path: " + path);
        }
    }

    private String getKeyFromPath(String path) {
        int index = path.lastIndexOf("/");
        if (index == -1) {
            return path;
        }
        return path.substring(0, index);
    }
}

public class Main {
    public static void main(String[] args) {
        FileNavigator navigator = new FileNavigator();

        FileData file1 = new FileData("files.txt", 1024, "/path/to/file");
        FileData file2 = new FileData("firstApp.java", 2048, "/path/to/file");

        navigator.add(file1);
        navigator.add(file2);

        List<FileData> fileList = navigator.find("/path/to/file");
        System.out.println("Files at /path/to/file:");
        for (FileData file : fileList) {
            System.out.println(file.getName());
        }

        List<FileData> filteredList = navigator.filterBySize("/path/to/file", 1500);
        System.out.println("Files at /path/to/file with size <= 1500 bytes:");
        for (FileData file : filteredList) {
            System.out.println(file.getName());
        }

        navigator.remove("/path/to/file");

        List<FileData> sortedList = navigator.sortBySize();
        System.out.println("All files sorted by size:");
        for (FileData file : sortedList) {
            System.out.println(file.getName() + " - " + file.getSize() + " bytes");
        }

        FileData inconsistentFile = new FileData("inconsistent.txt", 4096, "/another/path/");
        navigator.addWithConsistencyCheck(inconsistentFile);
    }
}

