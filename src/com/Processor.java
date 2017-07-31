package com;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Processor {
    Map<Status, Set<WorkOrder>> workOrderMap = new HashMap<>();

    public Map<Status, Set<WorkOrder>> getWorkOrderMap(){
        return workOrderMap;
    }

    public void processWorkOrders() {

        try {
            moveIt();
            readIt();
            Thread.sleep(5000L);
            processWorkOrders();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void moveIt() {
        // move work orders in map from one state to another

        Set<WorkOrder> workOrderSetInitial = workOrderMap.get(Status.INITIAL);
        Set<WorkOrder> workOrderSetAssigned = workOrderMap.get(Status.ASSIGNED);
        Set<WorkOrder> workOrderSetInProgress = workOrderMap.get(Status.IN_PROGRESS);
        Set<WorkOrder> workOrderSetDone = workOrderMap.get(Status.DONE);

        System.out.println("--------------------------");
        System.out.println("MAP (Before)" + workOrderMap);

        if(workOrderSetInitial.size() > 0) {
            for (Iterator iterator = workOrderSetInitial.iterator(); iterator.hasNext(); ) {
                workOrderSetAssigned.add( (WorkOrder) iterator.next() );
                workOrderMap.put( Status.ASSIGNED, workOrderSetAssigned );
                iterator.remove();
            }
        }else if(workOrderSetAssigned.size() > 0){
            for (Iterator iterator = workOrderSetAssigned.iterator(); iterator.hasNext(); ) {
                workOrderSetInProgress.add( (WorkOrder) iterator.next() );
                workOrderMap.put( Status.IN_PROGRESS, workOrderSetInProgress );
                iterator.remove();
            }
        }else if(workOrderSetInProgress.size() > 0){
            for (Iterator iterator = workOrderSetInProgress.iterator(); iterator.hasNext(); ) {
                workOrderSetDone.add( (WorkOrder) iterator.next() );
                workOrderMap.put( Status.DONE, workOrderSetDone );
                iterator.remove();
            }
        }
        System.out.println("--------------------------");
        System.out.println("MAP (After)" + workOrderMap);
    }

    private void readIt() {
        File currentDirectory = new File(".");
        File files[] = currentDirectory.listFiles();

        for (File f : files) {
            if (f.getName().endsWith(".json")) {
                ObjectMapper mapper = new ObjectMapper();
                try {
                    WorkOrder order = mapper.readValue(new File(f.getName()), WorkOrder.class);
                    order.setStatus(Status.INITIAL);

                    Set<WorkOrder> workOrderSet = workOrderMap.get(Status.INITIAL);
                    workOrderSet.add(order);

                    workOrderMap.put(Status.INITIAL, workOrderSet);
                    f.delete();
                    System.out.println(workOrderMap);

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void main(String args[]) {
        Processor processor = new Processor();

        //Initializing the map
        processor.workOrderMap.put(Status.INITIAL, new HashSet<>());
        processor.workOrderMap.put(Status.ASSIGNED, new HashSet<>());
        processor.workOrderMap.put(Status.IN_PROGRESS, new HashSet<>());
        processor.workOrderMap.put(Status.DONE, new HashSet<>());

        processor.processWorkOrders();
    }
}