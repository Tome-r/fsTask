import java.util.*;

interface IFileSystemItem {
    public void onRemove();
    public String name();
    public void print();
    public IFileSystemItem clone();
}

class File implements IFileSystemItem {
    public void onRemove() {}
    public String name() {
        return mName;
    }
    
    public void print() {
         System.out.println(name());
    }
    
    public IFileSystemItem clone() {
        return new File(mName);
    }
    
    private String mName;
    
    private File(String name, String extension) {
        mName = name + "." + extension;
    }
    
    private File(String name) {
        mName = name;
    }
    
    public static File createFile(String name, String extension) {
        // TODO: what if name or extension contain `.`?
        
        if (name == null || extension == null) {
            System.out.println("Failed. missing name: " + name + " or extensio: " + extension);
            return null;
        }
        else {
            File newFile = new File(name, extension);
            System.out.println("created new file: " + newFile.name()); 
            return newFile;
        }
    }
}

class Directory implements IFileSystemItem {
    
    private String mName;
    private Hashtable<String, IFileSystemItem> dirContent = new Hashtable<>();
    
    public void onRemove() {
        for (IFileSystemItem item : dirContent.values()) {
            item.onRemove();
        }
        
        dirContent.clear();
    }
    
    public String name() {
        return mName;
    }
    
    public void print() {
        System.out.println(name());
        for (IFileSystemItem item : dirContent.values()) {
            item.print();
        }
    }
    
    public IFileSystemItem clone() {
        Directory newDir = new Directory(mName);
        for (IFileSystemItem item : dirContent.values()) {
            newDir.addItem(item);
        }
        return newDir;
    }
    
    // copy item into folder
    public boolean addItem(IFileSystemItem item) {
        String itemName = item.name();
        if (dirContent.containsKey(itemName)) {
            System.out.println("already has item named: " + itemName);
            return false;
        }
        
        dirContent.put(itemName, item.clone());
        System.out.println("added item: " + itemName);
        return true;
    }

    public boolean removeItem(String itemName) {
        IFileSystemItem item = dirContent.get(itemName);
        if (item != null) {
            item.onRemove();
            dirContent.remove(itemName);
            System.out.println("removed item: " + itemName);
            return true;
        }
        else {
            System.out.println("failed to remove item: " + itemName + " no such item");
            return false;
        }
    }
    
    public IFileSystemItem get(String name) {
        return dirContent.get(name);
    }
    
    private Directory(String name) {
         mName = name;
    }
    
    static Directory createDirectory(String name) {
        if (name.contains(".")) {
            System.out.println("failed to create dir: " + name + ". invalid name");
            return null;
        }
        else {
           System.out.println("successfully created dir: " + name + ".");
           return new Directory(name);
        }
    }
}


public class MyClass {
    public static void main(String args[]) {
        // unit tests
        
        // File
        // file with no name or extension or both
        
        // Folder
        // folder with a dot(s) in its name
        // cannot add a file/folder with name that already exist - the operation will fail
        // if a folder was copied to another folder a change in one copy should not change the other copy
        // remove an item should remove it. and nothing else
        // remove() with a name that does not exist should not delete anything
        
        File f = File.createFile("a", "txt");
        // f.print();
        
        Directory d = Directory.createDirectory("dirdir");
        // d.print();
        
        d.addItem(f);
        // d.print();
        
        d.addItem(f);
        // d.print();
        
        d.addItem(d);
        d.print();
        
        d.removeItem("dirdir");
        d.print();
    }
}
 