import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;



public class MainClass {
    public static List<Navbar> readingCSVFile(){
        String csvFile = "src/Navigation.csv";BufferedReader br = null;
        String line = "";
        String cvsSplitBy = ";";
        List<Navbar> navigationMenuItems=new ArrayList<>();


        try {

            List<String> xmlLines = new BufferedReader(new FileReader(csvFile)) .lines() .skip(1).collect(Collectors.toList());
            for (String xmlLine : xmlLines) {
                line = xmlLine;

                String[] menuItem = line.split(cvsSplitBy);
                int parentId;
                if(menuItem[2].equals("NULL")){
                    parentId=0;
                }else{
                    parentId=Integer.parseInt(menuItem[2]);
                }
                Navbar navbar=new Navbar(Integer.parseInt(menuItem[0]),menuItem[1],parentId,Boolean.parseBoolean(menuItem[3]),menuItem[4]);
                navigationMenuItems.add(navbar);


            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return navigationMenuItems;
    }
    public static int countOccurrences(String occurrences, char findChar){
        return (int) occurrences.chars().filter(x -> {
            return x == findChar;
        }).count();
    }

    public static void orderedMenuItems(List<Navbar> navbar){

        List<Navbar> items= navbar.stream().filter(menuItem -> !menuItem.isHidden()).collect(Collectors.toList());//menu items that are not hidden
        List<Navbar> newItems = new ArrayList<>();
        List<String> results = new ArrayList<>();
        List<Integer> parentIds=new ArrayList<>();
        for (Navbar item:items){

            Navbar parentMenu=items.stream().filter(i->i.getId()==item.getParentId()).findAny().orElse(null);
            if(parentMenu==null){
                if(parentIds.contains(0)){
                    continue;
                }else{
                    parentIds.add(0);

                }
            }else {
                if (parentIds.contains(parentMenu.getId())) {
                    continue;
                } else {
                    parentIds.add(parentMenu.getId());
                }
            }

        }
        for(Integer pId:parentIds){
            List<Navbar> listSubMenuItems=items.stream().filter(i-> i.getParentId()==pId).sorted((ob1,ob2)->ob1.getMenuName().compareTo(ob2.getMenuName())).collect(Collectors.toList());
            if(pId==0){
                newItems.addAll(listSubMenuItems);
                continue;
            }
            Navbar parentMenu=items.stream().filter(i->i.getId()==pId).findAny().orElse(null);
            int index= newItems.indexOf(parentMenu);
            newItems.addAll(index+1,listSubMenuItems);

        }

        for(Navbar item:newItems){
            StringBuilder str = new StringBuilder();
            if(item.getParentId()==0){
                results.add("."+item.getMenuName());

            }else {

                Navbar parentMenu=items.stream().filter(i->i.getId()==item.getParentId()).findAny().orElse(null);
                String Parent=results.get(newItems.indexOf(parentMenu));
                int numberDots=countOccurrences(Parent,'.');

                str.append(".".repeat(Math.max(0, numberDots + 3)));
                    str.append(item.getMenuName());
                    results.add(str.toString());
                }


            }


        results.forEach(System.out::println);


    }

    public static void main(String[] args) {
                List<Navbar> navigationMenu=readingCSVFile();
                orderedMenuItems(navigationMenu);


    }
}
class Navbar{

    private int id;

    private String MenuName;

    private int parentId;

    private boolean isHidden;
    private String LinkURL;


    public Navbar() {
    }

    public Navbar(int id,String menuName, int parentId, boolean isHidden, String linkURL) {
        MenuName = menuName;
        this.parentId = parentId;
        this.isHidden = isHidden;
        LinkURL = linkURL;
        this.id=id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMenuName() {
        return MenuName;
    }

    public void setMenuName(String menuName) {
        MenuName = menuName;
    }

    public int getParentId() {
        return parentId;
    }

    public void setParentId(int parentId) {
        this.parentId = parentId;
    }

    public boolean isHidden() {
        return isHidden;
    }

    public void setHidden(boolean hidden) {
        isHidden = hidden;
    }

    public String getLinkURL() {
        return LinkURL;
    }

    public void setLinkURL(String linkURL) {
        LinkURL = linkURL;
    }
}

