/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


/**
 *
 * @author shivamgupta
 */

import java.io.*;

import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import org.apache.lucene.demo.*;
import org.apache.lucene.analysis.*;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.*;
import org.apache.lucene.search.*;
import org.apache.lucene.search.spans.SpanWeight;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.BytesRef;
import org.apache.lucene.util.Version;
import java.lang.String.*;
import static java.lang.System.exit;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;

import javax.swing.text.html.HTMLDocument.Iterator;

public class IndexFile {
            
    /**
     * @param args the command line arguments
     */
    static StandardAnalyzer analyzer = new StandardAnalyzer();
    static IndexWriterConfig config = new IndexWriterConfig(analyzer);
    static HashMap<String,LinkedList> invertedindex = new HashMap<String,LinkedList>();
    
    //File f1 = new File("output.txt");
    
    /**
     * @param args
     * @throws IOException
     */
     static FileWriter fw1;
    public static void main(String[] args) throws IOException {
        // TODO code application logic here
        String path = args[0];
        
       
        FileSystem fs = FileSystems.getDefault();
        Path path1 = fs.getPath(path);
        Directory ds = FSDirectory.open(path1);
        IndexReader reader = DirectoryReader.open(ds);
        
        Fields fields = MultiFields.getFields(reader);
        
        if(fields!= null){
           // System.out.println(reader.getDocCount(path));
        }
        File f = new File("inverted.txt");
        
        File f2 = new File("sgupta33_project2.txt");
        
        if(!f.exists()){
            f.createNewFile();
        }
        
        if(!f2.exists()){
            f.createNewFile();
        }
        FileWriter fw = new FileWriter(f);
        int i=0,j=0, k=0;
        
        
        for(String field: fields){
            
            Terms term = fields.terms(field);
//            if(term!=null){
//            System.out.println("no null terms");
//            }
            TermsEnum tenum = term.iterator();
            
            while(tenum.next()!=null){
                LinkedList<Integer> list = new LinkedList<Integer>();
                if(field.contains("_version_")|| field.contains("id")){
                    j++;
                }
                else{
                    i++;
                     BytesRef t = tenum.term();
                     PostingsEnum posting = MultiFields.getTermDocsEnum(reader, field, t, PostingsEnum.FREQS);
                     //int docnum = posting.nextDoc();
                     //System.out.println("\n Term"+t.utf8ToString());
                     while(posting.nextDoc()!= PostingsEnum.NO_MORE_DOCS){
                         int docid = posting.docID();
                         int freq = posting.freq();
                         list.add(docid);
                         
                        // System.out.print(docid + " ");
                     }
                     invertedindex.put(t.utf8ToString(), list);
                    fw.write("S_No.:"+i+" Field:"+field+" terms:"+t.utf8ToString()+"\n");
                    
                }
            }
            
            
            
        }
        fw.close();
        //System.out.println("version and ID count:"+j);
        //System.out.println("Total number of terms:"+i);
        //System.out.println("Total number of Docs:"+k);
        
      if(args.length>0){ 
    	  File f1 = new File(args[1]);
    	  if(!f1.exists()){
              f1.createNewFile();
          }
          
           fw1 = new FileWriter(f1);
          
      //System.out.println("args[0]: "+args[0]);
        File readfile = new File(args[2]);
        BufferedReader read = new BufferedReader(new InputStreamReader(new FileInputStream(readfile)));
        String line;
        if(read!=null){
            while((line=read.readLine())!=null){
                String[] termtoken = line.split("\\s+");
               // for(int m=0;m<termtoken.length;m++){System.out.println("Term: "+termtoken[m]);}
               
               getPostingsByTerm( termtoken); 
              // getPostingsANDTAAT(termtoken);
              // getPostingsORTAAT( termtoken);
                
            }
            fw1.close();
        }
    }
      else{
          System.out.println("No Argument Given");
      }
        
        
//        getPostingsByTerm("оротуканск");
      // getPostingsANDTAAT("afskrivning","amtssparekas");
     //   getPostingsORTAAT("оротуканск","ороша","орошен");

//        for(Entry<String,LinkedList> entry: invertedindex.entrySet() ){
//            System.out.println("Term: "+entry.getKey()+"DocID: "+entry.getValue()+"\n");
//        }
      	
      	//getPostingsANDDAAT("hesit","endomag","орошен");
      
      
    }
    
    public static void getPostingsByTerm(String... t) throws IOException{
        LinkedList<Integer> getposting = new LinkedList<Integer>();
       
        //System.out.println("GetPostings");
        for(int i=0;i<t.length;i++){
        getposting = invertedindex.get(t[i]);    
        	//System.out.println(t[i]);
           // System.out.println(getposting);
            fw1.write("GetPostings \n"+t[i]+"\n"+"Postings list: ");
            for(int j=0;j<getposting.size();j++){fw1.append(getposting.get(j)+" ");}
            fw1.append("\n");
        }
        getPostingsANDTAAT(t);
        getPostingsORTAAT(t);
        getPostingsANDDAAT(t);
        getPostingsORDAAT(t);
            
    }
    
    public static void getPostingsANDTAAT(String... term1) throws IOException{
        
       
        LinkedList getp1 = new LinkedList();
        LinkedList getp2 = new LinkedList();
        LinkedList result= new LinkedList();
        LinkedList finalresult= new LinkedList();
        int count=1;
        
        getp2 = invertedindex.get(term1[0]);
        
     x: for(int n=1;n<term1.length;n++){ 
        
        getp1 = invertedindex.get(term1[n]);
      
        int size1 = getp1.size();
        int size2 = getp2.size();
        int i=0,j=0;
        //System.out.println("getp2: "+getp2);
        while ((size1-1)!=i || (size2-1)!=j){
            
            int list1 = (int)getp1.get(i);
            int list2 = (int)getp2.get(j);
            //System.out.println("List1: "+list1+"list2: "+list2);
            if(list1==list2){
                result.add(getp1.get(i));
                i++;
                j++;
  
                count++;
            }
            else{
                if(list1>list2){
                    j++;
                    count++;
                }
                else{
                    i++;
                    count++;
                }
            }
       
        }
        if(result.size()!=0){ 
            getp2 = (LinkedList)result.clone(); 
        finalresult = (LinkedList)result.clone(); 
            
            while(!result.isEmpty()){result.removeFirst();}
    
        }
        else{
            break x;}
        
        
        }
        if(finalresult.isEmpty()){finalresult.addFirst("empty");}
        System.out.println("Result TAAT AND: "+finalresult);
       // System.out.println("Number of Comparisons: "+count);
        fw1.append("TAATAnd\n");
        for(int i=0;i<term1.length;i++){
        fw1.append(term1[i]+" ");
        }
        fw1.append("\nResults: ");
        for(int i=0;i<finalresult.size();i++){fw1.append(finalresult.get(i)+" ");}
        if(finalresult.getFirst()=="empty"){fw1.append("\nNumber of Documents in results: 0"+"\nNumber of Comparisons: "+count+"\n");
        }
        else{
        fw1.append("\nNumber of Documents in results: "+finalresult.size()+"\nNumber of Comparisons: "+count+"\n");
        }
        
    }
    
    public static void getPostingsORTAAT(String... terms) throws IOException{
            LinkedList<Integer> list1 = new LinkedList<Integer>();
            LinkedList<Integer> list2 = new LinkedList<Integer>();
            LinkedList<Integer> result = new LinkedList<Integer>();
            LinkedList<Integer> finalresult = new LinkedList<Integer>();
            
            int flag =0,count=1;
            
            list1= invertedindex.get(terms[0]);
            for(int i=1;i<terms.length;i++){   
            	int j=0,k=0;
            	list2= invertedindex.get(terms[i]);
            x:	while((list1.size()-1)>=j && (list2.size()-1)>=k){
            		int l1 = (int)list1.get(j);
            		int l2 = (int)list2.get(k);
            		if(l1==l2){
            			result.add(l1);
            			j++;
            			k++;
            			
            			if(j>=(list1.size())){
            				while(k<=(list2.size()-1)){
            					result.add(list2.get(k));
            					k++;
            				}
            				break x;
            			}
            			if(k>=(list2.size())){
            				while(j<=(list1.size()-1)){
            					result.add(list1.get(j));
            					j++;
            				}
            				break x;
            			}
            			
            		}
            		else{
            			if(l1<l2){
            				result.add(l1);      				
            				j++;
            				
            				if(j>=(list1.size())){
                				while(k<=(list2.size()-1)){
                					result.add(list2.get(k));
                					k++;
                				}
                				break x;
                			}
                			
            			}
            			else{
            				result.add(l2);           				
            				k++;
            				
            				
                			if(k>=(list2.size())){
                				while(j<=(list1.size()-1)){
                					result.add(list1.get(j));
                					j++;
                				}
                				break x;
                			}
            			}
            		}
            		count++;
            		
            	}
            	list1=(LinkedList)result.clone();
            	finalresult = (LinkedList)result.clone();
            	while(!result.isEmpty()){result.removeFirst();}
            }
            

            Collections.sort(finalresult);
            //System.out.println("Result TAAT OR: "+finalresult);
           // System.out.println("Comparison OR: "+count);
            fw1.append("TAATOr\n");
            for(int i=0;i<terms.length;i++){
            	fw1.append(terms[i]+" ");
            }
            fw1.append("\nResults: ");
            for(int i=0;i<finalresult.size();i++){fw1.append(finalresult.get(i)+" ");}
            fw1.append("\nNumbers of documents in results: "+finalresult.size()+"\nNumber of Comparisons: "+count+"\n");
           
        
            
    }
    
    public static void getPostingsANDDAAT(String... terms) throws IOException{
    	LinkedList<Integer> list = new LinkedList<Integer>();
        LinkedList<Integer> list1 = new LinkedList<Integer>();
        LinkedList<Integer> list2 = new LinkedList<Integer>();
        LinkedList result = new LinkedList();
        int comparisions=1;
        
        ArrayList<LinkedList> doclist = new ArrayList<LinkedList>();
         
        for(int i=0;i<terms.length;i++){
            list = invertedindex.get(terms[i]);
            doclist.add(list);
        }
        doclist = sortlist(doclist); //sorts the postings according to length of postings list
        //System.out.println("SORTED DOCLIST: "+doclist);
        
        list1 = doclist.get(0);
        
 x:       for(int i=0;i<list1.size();i++){
	 		int count=0;
	 		int element1 = list1.get(i);      
        	
 y:        	for(int j=1;j<doclist.size();j++){
        		list2 = doclist.get(j);
 z:       		for(int k=0;k<list2.size();k++){
        			int element2 = list2.get(k);
        			if(element1 == element2){
        				count++;
        				break z;
        			}
        			else{
        				if(element2>element1){
        					break y;
        				}
        			}
        			comparisions++;
        		}        		
        	}
	 		if(count==terms.length-1){
	 			result.add(element1);
	 		}
        }
        if(result.isEmpty()){result.addFirst("empty");}
        //System.out.println("DAAT AND: "+result+"\ncomparisions:"+comparisions);
        fw1.append("DAATAnd\n");
        for(int i=0;i<terms.length;i++){fw1.append(terms[i]+" ");}
        fw1.append("\nResults: ");
        for(int i=0;i<result.size();i++){fw1.append(result.get(i)+" ");}
        if(result.getFirst()=="empty"){fw1.append("\nNumber of Documents in Results: 0"+"\nNumber of Comparisons: "+comparisions+"\n");
        }
        else{
        fw1.append("\nNumber of Documents in Results: "+result.size()+"\nNumber of Comparisons: "+comparisions+"\n");
        }
    }
    
    public static ArrayList<LinkedList> sortlist(ArrayList<LinkedList> doclist){
    	
    	for(int i=1;i<doclist.size();i++){
    		for(int j=i;j>0;j--){
    			int size1 = doclist.get(j).size();
    			int size2 = doclist.get(j-1).size();
 	
    			LinkedList temp;
    			
    			if(size1<size2){
    				temp = (LinkedList)doclist.get(j).clone();
    				doclist.set(j,doclist.get(j-1));
    				doclist.set(j-1,temp);

    			}
    		}
    	}    	
    	return doclist;
    }
    
    public static void getPostingsORDAAT(String...terms) throws IOException{
    	LinkedList<Integer> list = new LinkedList<Integer>();
    	LinkedList<Integer> list1 = new LinkedList<Integer>();
    	LinkedList<Integer> list2 = new LinkedList<Integer>();
    	LinkedList<Integer> result = new LinkedList<Integer>();
    	LinkedList<Integer> semiresult = new LinkedList<Integer>();
    	
    	LinkedList<Integer> finalresult = new LinkedList<Integer>();
    	int count=0,flag=0;
    	
    	ArrayList<LinkedList> doclist = new ArrayList<LinkedList>();
    	
    	for(int i=0;i<terms.length;i++){
    		list = invertedindex.get(terms[i]);
    		doclist.add(list);
    	}
    	doclist = sortlist(doclist);
    	int size = doclist.size()-1;
    	list1 = doclist.get(size);
    	
 x:   	for(int i=0;i<list1.size();i++){
    		
    		int element1 = list1.get(i);
    	//	System.out.println("List1 value: "+list1+"i: "+i);
    		
 y:   		for(int j=doclist.size()-2;j>=0;j--){
    			list2 = doclist.get(j);
    		//	System.out.println("List2 value: "+list2+"j: "+j);
 z:   			for(int k=0;k<list2.size();k++){
    				int element2 = list2.get(k);
    				
    				if(element1== element2){
//    					result.add(element1);
    					break z;
    				}
    				else{
    					if(element2>element1){
//    						result.add(element1);
    						if(element1==list1.getLast()){
    							semiresult.add(element2);
    						}
    						break z;
    					}
    					else{
    						for(int m=0;m<result.size();m++){
    							if(element2 == result.get(m)){
    								flag=1;
    								break;
    							}
    						}
    						if(flag==0){result.add(element2);}
    						else{flag=0;}
    						//result.add(element2);
    						
    					}
    				}
    		//		System.out.println("Result: "+result+"k: "+k);
    			}
    			
    			count++;
    			
    		}
    		result.add(element1);
    		if(!semiresult.isEmpty()){
    			Collections.sort(semiresult);
    			result.addAll(semiresult);
    		}
//    		finalresult = (LinkedList)result.clone();
//    		while(!result.isEmpty()){result.removeFirst();}
    		
    	}
    	//System.out.println("DAAT OR: "+result+"\nComparisions: "+count+"Size: "+result.size());
    	fw1.append("DAATOr\n");
    	for(int i=0;i<terms.length;i++){fw1.append(terms[i]+" ");}
    	fw1.append("\nResults: ");
        for(int i=0;i<result.size();i++){fw1.append(result.get(i)+" ");}
    	fw1.append("\nNumber of Documents in the results: "+result.size()+"\nNumber of Comparisons: "+count+"\n");
    }
}
