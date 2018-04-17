package homework7;

import java.util.Optional;

//Please forgive this horrible hack job.
public class Homework7 {

    public static void main(String[] args) {
        Optional[][] initialMap = {{Optional.of(0)              ,Optional.ofNullable(null)      ,Optional.of(3)                 ,Optional.ofNullable(null)      ,Optional.ofNullable(null)},
                                  {Optional.of(-4)              ,Optional.of(0)                 ,Optional.of(2)                 ,Optional.ofNullable(null)      ,Optional.ofNullable(null)},
                                  {Optional.ofNullable(null)    ,Optional.of(-1)                ,Optional.of(0)                 ,Optional.ofNullable(7)         ,Optional.ofNullable(5)},
                                  {Optional.ofNullable(null)    ,Optional.of(1)                 ,Optional.ofNullable(null)      ,Optional.ofNullable(0)         ,Optional.ofNullable(10)},
                                  {Optional.ofNullable(null)    ,Optional.ofNullable(null)      ,Optional.ofNullable(null)      ,Optional.ofNullable(-8)        ,Optional.ofNullable(0)}
                                  };
        Boolean  shortestPath = floydWarshall(initialMap);
        String response = shortestPath == null ? 
                "The input was invalid": 
                shortestPath ? 
                    "There was a shortest path found Check Output of final table": 
                    "There was a negative weight cycle so no solution";  
        System.out.println(response);
    }
    
    
    private static Boolean floydWarshall(Optional<Integer>[][] initialMap){
        Integer vertexNumber = initialMap == null ? null : initialMap[0].length;
        if(vertexNumber == null){
            return null;
        }
        
        Optional<Integer>[][][] warshalMap = new Optional[vertexNumber][vertexNumber][vertexNumber];
        
        for(int i = 0 ; i < vertexNumber; i++){
            for(int j = 0 ; j < vertexNumber; j++){
                warshalMap[0][i][j] = initialMap[i][j]; 
            }
        }
        
        System.out.println("Original input map of weights from vertex to vertex");
        printLevel(warshalMap, 0);
        
        Boolean changed = true;
        Boolean noNegValues = true;
        for(int w = 1 ; (w < vertexNumber) && changed && noNegValues; w++){
            System.out.println("Entering into Map level "+ w);
            for(int i = 0 ; i < vertexNumber; i++){
                System.out.println("\tMoving through row "+ i);
            
                for(int j =0 ; j < vertexNumber; j++ ){
                    System.out.println("\t\tChecking cell "+ j);
            
                    Optional<Integer> pastVal = warshalMap[w-1][i][j];
                    Optional<Integer> pastLeft = warshalMap[w-1][i][w];
                    Optional<Integer> pastRight = warshalMap[w-1][w][j];
                    Optional<Integer> newVal;
                    if(pastLeft.isPresent()){
                        if(pastRight.isPresent()){
                            newVal = Optional.of(pastLeft.get() + pastRight.get());
                        }else{
                            newVal = Optional.empty();
                        }
                    }else{
                        newVal = Optional.empty();
                    }
                    if(pastVal.isPresent()){
                        if(newVal.orElse(Integer.MAX_VALUE)< pastVal.orElse(Integer.MAX_VALUE)){
                            warshalMap[w][i][j] = newVal;
                        }else{
                            warshalMap[w][i][j] = pastVal;
                        }
                    }else{
                        warshalMap[w][i][j] = newVal;
                    }
                    if( i == j && warshalMap[w][i][j].orElse(0) < 0){
                           noNegValues = false;
                           System.out.println("\n!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
                           System.out.println(String.format("Cell: warshalMap[%s][%s][%s] in map has gone from a weight %s to a negative weight %s indicating a self loop where there is a negative weight cycle when including node %s.", w,i,j,warshalMap[w-1][i][j].get(),warshalMap[w][i][j].get(), NODES.values()[w]));
                           System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!\n");
                           
                    }
                    System.out.println(
                            String.format("\t\tMin between warshalMap[%s-1][%s][%s]: %s and warshalMap[%s-1][%s][%s]:%s + warshalMap[%s-1][%s][%s]:%s"
                                    + "\n New value for warshalMap[%s][%s][%s] is %s"
                                    ,w,i,j,pastVal.isPresent()? pastVal.get().toString() : "null" 
                                    ,w,i,w,pastLeft.isPresent()? pastLeft.get().toString() : "null" 
                                    ,w,w,j,pastRight.isPresent()? pastRight.get().toString() : "null" 
                                    ,w,i,j,warshalMap[w][i][j].isPresent()? warshalMap[w][i][j].get().toString() : "null"));
                    
                }
            }
            printLevel(warshalMap, w);
        }
        return noNegValues;
    }

    private static String paddedValue(String string, int width){
        String value = "";
        int padding = (width - string.length())/2;
                
        for(int k = 0; k < padding + ((width - string.length())%2); k++){//+ ((width - value.length())%2)
            value = value.concat(" ");
        }
            value = value.concat(string);
        for(int k = 0; k < padding ; k++){
            value = value.concat(" ");
        }
        return value;
    }
    
    private static void printTop(int elements,int width){
        String top = paddedValue(" ", width) + " ";  
        
        for(int j = 0; j < elements; j++){
                String value = NODES.values()[j].name();  
                top = top.concat(paddedValue(value, width)) + " ";
        }
        System.out.println(top);
        printBetween(elements,width);
    }
    
    private static void printBetween(int elements,int width){
        System.out.print(paddedValue(" ", width));
        for(int i = 0 ; i < (elements*(width + 1));i++) System.out.print("-");            
        System.out.println();
    }
    
    private static void printLevel(Optional<Integer>[][][] warshalMap, int depth){
        int width = 10;
        printTop(warshalMap.length, width);
        for(int i = 0 ; i < warshalMap.length; i++){
            System.out.println();
            String level = paddedValue(NODES.values()[i].name(), width) + "|";
            for(int j = 0; j < warshalMap.length; j++){
                String value = warshalMap[depth][i][j].isPresent() ? warshalMap[depth][i][j].get().toString() : "NULL";
                level = level.concat(paddedValue(value, width));
                level = level.concat("|");
            }
            System.out.println(level);
            printBetween(warshalMap.length, width);
        } 
        System.out.println();  
    }

    private static enum NODES {a,b,c,d,e,f,g,h,i,j,k,l,m,n,o,p,q,r,s,t,u,v,w,x,y,z};
}
