//==================================================
//
//  Copyright 2017 Siemens Product Lifecycle Management Software Inc. All Rights Reserved.
//
//==================================================

package com.teamcenter.hello;



import com.teamcenter.clientx.AppXSession;
import com.teamcenter.schemas.soa._2006_03.exceptions.ServiceException;

//Include the Saved Query Service Interface
import com.teamcenter.services.loose.query.SavedQueryService;

// Input and output structures for the service operations
// Note: the different namespace from the service interface
import com.teamcenter.services.loose.query._2006_03.SavedQuery.GetSavedQueriesResponse;
import com.teamcenter.services.loose.query._2007_09.SavedQuery.QueryResults;
import com.teamcenter.services.loose.query._2007_09.SavedQuery.SavedQueriesResponse;
import com.teamcenter.services.loose.query._2008_06.SavedQuery.QueryInput;
import com.teamcenter.services.loose.core.DataManagementService;
import com.teamcenter.soa.client.model.ModelObject;
import com.teamcenter.soa.client.model.ServiceData;


public class Query
{

    /**
     * Perform a simple query of the database
     *
     */
    public void queryItems()
    {

        ModelObject query = null;

        // Get the service stub
        SavedQueryService queryService = SavedQueryService.getService(AppXSession.getConnection());
        DataManagementService dmService= DataManagementService.getService(AppXSession.getConnection());

        try
        {

            // *****************************
            // Execute the service operation
            // *****************************
            GetSavedQueriesResponse savedQueries = queryService.getSavedQueries();


            if (savedQueries.queries.length == 0)
            {
                System.out.println("There are no saved queries in the system.");
                return;
            }

            // Find one called 'Item Name'
            for (int i = 0; i < savedQueries.queries.length; i++)
            {

                if (savedQueries.queries[i].name.equals("Item Name"))
                {
                    query = savedQueries.queries[i].query;
                    break;
                }
            }
        }
        catch (ServiceException e)
        {
            System.out.println("GetSavedQueries service request failed.");
            System.out.println(e.getMessage());
            return;
        }

        if (query == null)
        {
            System.out.println("There is not an 'Item Name' query.");
            return;
        }
        try
        {
            // Search for all Items, returning a maximum of 25 objects
            QueryInput[] savedQueryInput = new QueryInput[1];
            savedQueryInput[0] = new QueryInput();
            savedQueryInput[0].query = query;
            savedQueryInput[0].maxNumToReturn = 25;
            savedQueryInput[0].limitList = new ModelObject[0];
            savedQueryInput[0].entries = new String[]{"Item Name" };
            savedQueryInput[0].values = new String[1];
            savedQueryInput[0].values[0] = "*";
            
            //*****************************
            //Execute the service operation
            //*****************************
            SavedQueriesResponse savedQueryResult = queryService.executeSavedQueries(savedQueryInput);
            QueryResults found = savedQueryResult.arrayOfResults[0];
            
            System.out.println("");
            System.out.println("Found Items:");
            
            // Page through the results 10 at a time
            for(int i=0; i< found.objectUIDS.length; i+=10)
            {
                int pageSize = (i+10<found.objectUIDS.length)? 10:found.objectUIDS.length-i;
            
                String[] uids = new String[pageSize];
                for(int j=0; j<pageSize; j++)
                {
                    uids[j]= found.objectUIDS[i+j];
                }
                ServiceData sd = dmService.loadObjects( uids );
                ModelObject[] foundObjs = new ModelObject[ sd.sizeOfPlainObjects()];
                for( int k =0; k< sd.sizeOfPlainObjects(); k++)
                {
                    foundObjs[k] = sd.getPlainObject(k);
                }

                AppXSession.printObjects( foundObjs );
            }
        }
        catch (Exception e)
        {
             System.out.println("ExecuteSavedQuery service request failed.");
             System.out.println(e.getMessage());
             return;
        }

    }

}
