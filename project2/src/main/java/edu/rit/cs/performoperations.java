package edu.rit.cs;

public class performoperations extends Thread {

    public PubSubAgent em;
    public int option;
    public int operations_option;
    public Topic newtopic = new Topic();
    public Event event = new Event();
    public String keyword = "";

    public performoperations(PubSubAgent em, int option, int operations_option,Topic newtopic){
        this.em = em;
        this.option = option;
        this.operations_option = operations_option;
        this.newtopic = newtopic;
    }

    public performoperations(PubSubAgent em, int option, int operations_option){
        this.em = em;
        this.option = option;
        this.operations_option = operations_option;
    }

    public performoperations(PubSubAgent em, int option, int operations_option,Event event){
        this.em = em;
        this.option = option;
        this.operations_option = operations_option;
        this.event = event;
    }

    public performoperations(PubSubAgent em, int option, int operations_option,String keyword){
        this.em = em;
        this.option = option;
        this.operations_option = operations_option;
        this.keyword = keyword;
    }

    public void run(){
        if(this.option==1){
            if(this.operations_option == 1){
                System.out.println("advertising--------------");
                this.em.advertise(newtopic);
            }else{
                System.out.println("publishing--------------");
                this.em.publish(event);
            }
        }else{
            if(this.operations_option == 1){
                System.out.println("subscribing with topic--------------");
                this.em.subscribe(newtopic);
            }else if(this.operations_option == 2){
                System.out.println("subscribing with keyword--------------");
                this.em.subscribe(this.keyword);
            }else if(this.operations_option == 3){
                System.out.println("un subscribing with topic--------------");
                this.em.unsubscribe(this.newtopic);
            }else if(this.operations_option == 4){
                System.out.println("unsubscribing to all topics--------------");
                this.em.unsubscribe();
            }else if(this.operations_option == 5){
                System.out.println("listing all topics--------------");
                this.em.listSubscribedTopics();
            }

        }
    }
}
