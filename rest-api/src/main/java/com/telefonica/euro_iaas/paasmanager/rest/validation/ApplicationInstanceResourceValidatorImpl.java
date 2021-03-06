/**
 * Copyright 2014 Telefonica Investigación y Desarrollo, S.A.U <br>
 * This file is part of FI-WARE project.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License.
 * </p>
 * <p>
 * You may obtain a copy of the License at:<br>
 * <br>
 * http://www.apache.org/licenses/LICENSE-2.0
 * </p>
 * <p>
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * </p>
 * <p>
 * See the License for the specific language governing permissions and limitations under the License.
 * </p>
 * <p>
 * For those usages not covered by the Apache version 2.0 License please contact with opensource@tid.es
 * </p>
 */

package com.telefonica.euro_iaas.paasmanager.rest.validation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.telefonica.fiware.commons.dao.EntityNotFoundException;
import com.telefonica.fiware.commons.dao.InvalidEntityException;
import com.telefonica.euro_iaas.paasmanager.exception.ApplicationInstanceNotFoundException;
import com.telefonica.euro_iaas.paasmanager.exception.InvalidApplicationReleaseException;
import com.telefonica.euro_iaas.paasmanager.manager.ApplicationInstanceManager;
import com.telefonica.euro_iaas.paasmanager.manager.EnvironmentInstanceManager;
import com.telefonica.euro_iaas.paasmanager.model.dto.ApplicationReleaseDto;

/**
 * Validator of the application instance resource.
 */
public class ApplicationInstanceResourceValidatorImpl implements ApplicationInstanceResourceValidator {

    private ApplicationInstanceManager applicationInstanceManager;
    private EnvironmentInstanceManager environmentInstanceManager;
   
    /** The log. */
    private static Logger log = LoggerFactory.getLogger(ApplicationInstanceResourceValidatorImpl.class);

    /**
     * Validate the installation of a new application.
     * @param vdc   The vdc id.
     * @param environmentInstance   The environment instance id.
     * @param applicationReleaseDto The application release id.
     * @throws InvalidApplicationReleaseException
     * @throws ApplicationInstanceNotFoundException
     */
    public void validateInstall(String vdc, String environmentInstance, ApplicationReleaseDto applicationReleaseDto)
        throws InvalidApplicationReleaseException, ApplicationInstanceNotFoundException {

        if ((applicationReleaseDto.getApplicationName() == null) || (applicationReleaseDto.getVersion() == null)) {
            throw new InvalidApplicationReleaseException("Application Name is not provided");
        }

        // Check that the application is not installed already
        try {
            applicationInstanceManager
                    .load(vdc, applicationReleaseDto.getApplicationName() + "-" + environmentInstance);
            throw new InvalidApplicationReleaseException("Application already installed");
        } catch (EntityNotFoundException e) {
            log.error("Entity not found.");
        }

    }

    /**
     * Validate the uninstall operation.
     * @param vdc   The vdc id.
     * @param environmentInstance   The environment instance id.
     * @param applicationName   The applicationa name id.
     * @throws InvalidEntityException
     */
    public void validateUnInstall(String vdc, String environmentInstance, String applicationName)
        throws InvalidEntityException {
        
    	// Check the environment and application exists
    	try {
            environmentInstanceManager.load(vdc, environmentInstance);
        } catch (EntityNotFoundException e) {
            String mens = "The environmetn instance "
                    + environmentInstance
                    + " does not exists for uninstalling application";

            log.warn(mens);
            throw new InvalidEntityException(mens);
        }

    	try {
            applicationInstanceManager.load(vdc, applicationName);
        } catch (EntityNotFoundException e) {
            String mens = "The application instance "
                    + applicationName
                    + " does not exists for uninstalling application";

            log.warn(mens);
            throw new InvalidEntityException(mens);
        }
    }



    public void setApplicationInstanceManager(ApplicationInstanceManager applicationInstanceManager) {
        this.applicationInstanceManager = applicationInstanceManager;
    }
    
    public void setEnvironmentInstanceManager(EnvironmentInstanceManager environmentInstanceManager) {
    	this.environmentInstanceManager = environmentInstanceManager;
    }

}
