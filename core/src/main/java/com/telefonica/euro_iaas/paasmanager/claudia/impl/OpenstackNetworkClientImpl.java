/**
 * (c) Copyright 2013 Telefonica, I+D. Printed in Spain (Europe). All Rights Reserved.<br>
 * The copyright to the software program(s) is property of Telefonica I+D. The program(s) may be used and or copied only
 * with the express written consent of Telefonica I+D or in accordance with the terms and conditions stipulated in the
 * agreement/contract under which the program(s) have been supplied.
 */

package com.telefonica.euro_iaas.paasmanager.claudia.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.telefonica.euro_iaas.commons.dao.EntityNotFoundException;
import com.telefonica.euro_iaas.paasmanager.claudia.NetworkClient;
import com.telefonica.euro_iaas.paasmanager.exception.InfrastructureException;
import com.telefonica.euro_iaas.paasmanager.exception.OpenStackException;
import com.telefonica.euro_iaas.paasmanager.model.ClaudiaData;
import com.telefonica.euro_iaas.paasmanager.model.Network;
import com.telefonica.euro_iaas.paasmanager.model.NetworkInstance;
import com.telefonica.euro_iaas.paasmanager.model.RouterInstance;
import com.telefonica.euro_iaas.paasmanager.model.SubNetworkInstance;
import com.telefonica.euro_iaas.paasmanager.util.OpenStackUtil;

/**
 * @author henar.munoz
 */
public class OpenstackNetworkClientImpl implements NetworkClient {

    private OpenStackUtil openStackUtil = null;
    private static Logger log = Logger.getLogger(OpenstackNetworkClientImpl.class);


    /**
     * It adds the network to the router.
     * 
     * @params claudiaData
     * @params router
     * @params network
     * @throws InfrastructureException
     */


    public void addNetworkToRouter(ClaudiaData claudiaData, RouterInstance router, NetworkInstance netInstance) throws InfrastructureException {
        log.info("Add Interfact from net " + netInstance.getNetworkName() + " to router " + router.getName()
                + " for user " + claudiaData.getUser().getTenantName());
        try {
            String response = openStackUtil.addInterface(router.getIdRouter(), netInstance, claudiaData.getUser());
            log.debug(response);
        } catch (OpenStackException e) {
            String msm = "Error to deploy the network " + router.getIdRouter()+ ":" + e.getMessage();
            log.error(msm);
            throw new InfrastructureException(msm, e);
        }

    }
    
    /**
     * It adds the network to the public router.
     * 
     * @params net
     * @throws InfrastructureException
     */

    public void addNetworkToPublicRouter(ClaudiaData claudiaData, NetworkInstance netInstance) throws InfrastructureException {
        log.info("Add Interfact from net " + netInstance.getNetworkName() + " to public router ");

        try {
            String response = openStackUtil.addInterfaceToPublicRouter(claudiaData.getUser(), netInstance);
            log.debug(response);
        } catch (OpenStackException e) {
            String msm = "Error to add the network " + netInstance.getNetworkName() + " to the public router :" + e.getMessage();
            log.error(msm);
            throw new InfrastructureException(msm, e);
        }

    }

    /**
     * It deletes the interface of the network in the router.
     */

    public void deleteNetworkFromRouter(ClaudiaData claudiaData, RouterInstance router, NetworkInstance net)
    throws InfrastructureException {
        log.info("Delete Interfact net " + net.getNetworkName()+ " " + net.getIdNetRouter() + " from router " + router.getName()
                + " for user " + claudiaData.getUser().getTenantName());
        try {

            String response = openStackUtil.removeInterface(router, net.getIdNetRouter(), claudiaData.getUser());
            log.debug(response);
        } catch (OpenStackException e) {
            String msm = "Error to deploy the network " + router.getName() + ":" + e.getMessage();
            log.error(msm);
            throw new InfrastructureException(msm, e);
        }

    }

    /**
     * The deploy the network in Openstack.
     * 
     * @params claudiaData
     * @params network
     */
    public void deployNetwork(ClaudiaData claudiaData, NetworkInstance networkInstance) throws InfrastructureException {
        log.info("Deploy network " + networkInstance.getNetworkName() + " for user " + claudiaData.getUser().getTenantName());
        log.debug("Payload " + networkInstance.toJson());
        String response;
        try {
            response = openStackUtil.createNetwork(networkInstance, claudiaData.getUser());
            log.debug(response);
            // "network-" + claudiaData.getUser().getTenantName()
            JSONObject networkString = new JSONObject(response);
            String id = networkString.getJSONObject("network").getString("id");
            log.debug("Network id " + id);
            networkInstance.setIdNetwork(id);
        } catch (OpenStackException e) {
            String msm = "Error to deploy the network " + networkInstance.getNetworkName() + ":" + e.getMessage();
            log.error(msm);
            throw new InfrastructureException(msm, e);
        } catch (JSONException e) {
            String msm = "Error to obtain the id of the network " + networkInstance.getNetworkName() + ":" + e.getMessage();
            log.error(msm);
            throw new InfrastructureException(msm, e);
        }
    }
    /**
     * The deploy the network in Openstack.
     * 
     * @params claudiaData
     * @params network
     */
    public void deployRouter(ClaudiaData claudiaData, RouterInstance router) throws InfrastructureException {
        log.info("Deploy router " + router.getName() + " for user " + claudiaData.getUser().getTenantName());

        try {
            log.debug("Payload " + router.toJson());
            String response = openStackUtil.createRouter(router, claudiaData.getUser());


            JSONObject networkString = new JSONObject(response);
            String id = networkString.getJSONObject("router").getString("id");
            log.debug("Router id " + id);
            router.setIdRouter(id);
        } catch (OpenStackException e) {
            String msm = "Error to deploy the network " + router.getName() + ":" + e.getMessage();
            log.error(msm);
            throw new InfrastructureException(msm, e);
        } catch (JSONException e) {
            String msm = "Error to obtain the id of the network " + router.getName() + ":" + e.getMessage();
            log.error(msm);
            throw new InfrastructureException(msm, e);
        }
    }

    /**
     * The deploy the subnet in Openstack.
     * 
     * @params claudiaData
     * @params subNet
     */
    public void deploySubNetwork(ClaudiaData claudiaData, SubNetworkInstance subNet) throws InfrastructureException {
        log.info("Deploy subNetworknetwork " + subNet.getName() + " for user " + claudiaData.getUser().getTenantName());
        log.debug("Payload " + subNet.toJson());
        String response;
        try {
            response = openStackUtil.createSubNet(subNet, claudiaData.getUser());
            // "network-" + claudiaData.getUser().getTenantName()
            JSONObject networkString = new JSONObject(response);
            log.debug(response);

            String id = networkString.getJSONObject("subnet").getString("id");
            log.debug("Network id " + id);
            subNet.setIdSubNet(id);
        } catch (OpenStackException e) {
            String msm = "Error to deploy the network " + subNet.getName() + ":" + e.getMessage();
            log.error(msm);
            throw new InfrastructureException(msm, e);
        } catch (JSONException e) {
            String msm = "Error to obtain the id of the network " + subNet.getName() + ":" + e.getMessage();
            log.error(msm);
            throw new InfrastructureException(msm, e);
        }
    }

    /**
     * It destroys the network.
     * 
     * @params claudiaData
     * @params network
     */
    public void destroyNetwork(ClaudiaData claudiaData, NetworkInstance networkInstance) throws InfrastructureException {

        try {
            openStackUtil.deleteNetwork(networkInstance.getIdNetwork(), claudiaData.getUser());
        } catch (OpenStackException e) {
            String msm = "Error to delete the network " + networkInstance.getNetworkName() + ":" + e.getMessage();
            log.error(msm);
            throw new InfrastructureException(msm, e);
        }
    }

    /**
     * It delete the router in Openstack.
     */
    public void destroyRouter(ClaudiaData claudiaData, RouterInstance router) throws InfrastructureException {
        try {
            openStackUtil.deleteRouter(router.getIdRouter(), claudiaData.getUser());
        } catch (OpenStackException e) {
            String msm = "Error to delete the router " + router.getName() + ":" + e.getMessage();
            log.error(msm);
            throw new InfrastructureException(msm, e);
        }

    }

    /**
     * It destroys the network.
     * 
     * @params claudiaData
     * @params network
     */
    public void destroySubNetwork(ClaudiaData claudiaData, SubNetworkInstance subnet) throws InfrastructureException {

        try {
            openStackUtil.deleteSubNetwork(subnet.getIdSubNet(), claudiaData.getUser());
        } catch (OpenStackException e) {
            String msm = "Error to delete the network " + subnet.getName() + ":" + e.getMessage();
            log.error(msm);
            throw new InfrastructureException(msm, e);
        }
    }

    /**
     * It loads all networks.
     * 
     * @params claudiaData
     */
    public List<NetworkInstance> loadAllNetwork(ClaudiaData claudiaData) throws InfrastructureException {
    	List<NetworkInstance> networks = new ArrayList<NetworkInstance>  ();
    	try {
            String response= openStackUtil.listNetworks(claudiaData.getUser());
            JSONObject lNetworkString = new JSONObject(response);
            JSONArray jsonNetworks = lNetworkString.getJSONArray("networks");
            
            for (int i = 0; i< jsonNetworks.length(); i++) {
            	
            	JSONObject jsonNet = jsonNetworks.getJSONObject(i);
            	String name = (String)jsonNet.get("name");
            	NetworkInstance netInst = new NetworkInstance(name);
            	networks.add(netInst);
            }

        } catch (OpenStackException e) {
            String msm = "Error to get the networks :" + e.getMessage();
            log.error(msm);
            throw new InfrastructureException(msm, e);
        } catch (JSONException e) {
            String msm = "Error to get the networks :" + e.getMessage();
            log.error(msm);
            throw new InfrastructureException(msm, e);
        }
        return networks;
    }

    /**
     * It obtains information about the network.
     * 
     * @params claudiaData
     * @params network
     * @return network information
     */
    public String loadNetwork(ClaudiaData claudiaData, NetworkInstance network) throws EntityNotFoundException {
        String response = "";
        try {
            response = openStackUtil.getNetworkDetails(network.getIdNetwork(), claudiaData.getUser());
        } catch (OpenStackException e) {
            String msm = "Error to obtain the network infromation " + network.getNetworkName() + ":" + e.getMessage();
            log.error(msm);
            throw new EntityNotFoundException(Network.class, msm, e);
        }
        return response;
    }

    /**
     * Set the variable.
     * 
     * @params openStackUtil
     */
    public void setOpenStackUtil(OpenStackUtil openStackUtil) {
        this.openStackUtil = openStackUtil;
    }


    /**
     * It load the subNet.
     */
	public String loadSubNetwork(ClaudiaData claudiaData,
			SubNetworkInstance subNet) throws EntityNotFoundException {
	    String response = "";
	    try {
	        response = openStackUtil.getSubNetworkDetails(subNet.getIdNetwork(), claudiaData.getUser());
	    } catch (OpenStackException e) {
	        String msm = "Error to obtain the network infromation " + subNet.getName()+ ":" + e.getMessage();
	        log.error(msm);
	        throw new EntityNotFoundException(Network.class, msm, e);
	    }
	    return response;
	}

	/**
	 * it delete the interface in the public router
	 * @throws InfrastructureException 
	 */
	public void deleteNetworkToPublicRouter(ClaudiaData claudiaData,
		NetworkInstance netInstance) throws InfrastructureException {
		log.info("Delete Interfact from net " + netInstance.getNetworkName() + " to public router ");

        try {
            String response = openStackUtil.deleteInterfaceToPublicRouter(claudiaData.getUser(), netInstance);
            log.debug(response);
        } catch (OpenStackException e) {
            String msm = "Error to add the network " + netInstance.getNetworkName() + " to the public router :" + e.getMessage();
            log.error(msm);
            throw new InfrastructureException(msm, e);
        }
	}

}