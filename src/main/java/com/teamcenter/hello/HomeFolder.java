//==================================================
//
//  Copyright 2017 Siemens Product Lifecycle Management Software Inc. All Rights Reserved.
//
//==================================================

package com.teamcenter.hello;

import com.teamcenter.clientx.AppXSession;
import com.teamcenter.services.loose.core.DataManagementService;
import com.teamcenter.soa.client.model.ModelObject;
import com.teamcenter.soa.exceptions.NotLoadedException;

public class HomeFolder
{

    /**
     * List the contents of the Home folder.
     *
     */
    public void listHomeFolder(ModelObject user)
    {
        ModelObject home = null;
        ModelObject[] contents = null;

        // Get the service stub
        DataManagementService dmService = DataManagementService.getService(AppXSession.getConnection());

        try
        {
            // User was a primary object returned from the login command
            // the Object Property Policy should be configured to include the
            // 'home_folder' property. However the actual 'home_folder' object
            // was a secondary object returned from the login request and
            // therefore does not have any properties associated with it. We will need to
            // get those properties explicitly with a 'getProperties' service request.
            home = user.getPropertyObject("home_folder").getModelObjectValue();
        }
        catch (NotLoadedException e)
        {
            System.out.println(e.getMessage());
            System.out.println("The Object Property Policy ($TC_DATA/soa/policies/Default.xml) is not configured with this property.");
            return;
        }

        try
        {
            ModelObject[] objects = { home };
            String[] attributes = { "contents" };

            // *****************************
            // Execute the service operation
            // *****************************
            dmService.getProperties(objects, attributes);


            // The above getProperties call returns a ServiceData object, but it
            // just has pointers to the same object we passed into the method, so the
            // input object have been updated with new property values
            contents = home.getPropertyObject("contents").getModelObjectArrayValue();
        }
        // This should never be thrown, since we just explicitly asked for this
        // property
        catch (NotLoadedException e){}

        System.out.println("");
        System.out.println("Home Folder:");
        AppXSession.printObjects( contents );

    }

}
