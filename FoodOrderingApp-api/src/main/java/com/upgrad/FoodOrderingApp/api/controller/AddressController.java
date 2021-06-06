package com.upgrad.FoodOrderingApp.api.controller;

import com.upgrad.FoodOrderingApp.api.model.*;
import com.upgrad.FoodOrderingApp.service.businness.AddressService;
import com.upgrad.FoodOrderingApp.service.businness.CustomerService;
import com.upgrad.FoodOrderingApp.service.entity.AddressEntity;
import com.upgrad.FoodOrderingApp.service.entity.CustomerEntity;
import com.upgrad.FoodOrderingApp.service.entity.StateEntity;
import com.upgrad.FoodOrderingApp.service.exception.AddressNotFoundException;
import com.upgrad.FoodOrderingApp.service.exception.AuthorizationFailedException;
import com.upgrad.FoodOrderingApp.service.exception.SaveAddressException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import static com.upgrad.FoodOrderingApp.api.util.Constants.BEARER_AUTH;

/**
 * Controller to Handle all Address Related Endpoints
 */
@CrossOrigin
@RestController
@RequestMapping("/")
public class AddressController {
    @Autowired
    AddressService addressService;

    @Autowired
    CustomerService customerService;

    /**
     * The method handles Address save Related request.It takes the details as per in the SaveAddressRequest
     * & produces response in SaveAddressResponse and returns UUID of newly Created Customer Address and
     * Success message else Return error code and error Message.
     *
     * @param authorization      Authorization Details
     * @param saveAddressRequest Address to be Saved
     * @return Saved Address
     * @throws AuthorizationFailedException Authorization Failed Exception
     * @throws AddressNotFoundException     Address Not FoundException
     * @throws SaveAddressException         Save Address Exception
     */
    @CrossOrigin
    @PostMapping(path = "/address", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<SaveAddressResponse> saveAddress(@RequestHeader("authorization") final String authorization, @RequestBody(required = false) SaveAddressRequest saveAddressRequest) throws AuthorizationFailedException, AddressNotFoundException, SaveAddressException {
        String accessToken = authorization.split(BEARER_AUTH)[1];

        CustomerEntity customerEntity = customerService.getCustomer(accessToken);

        AddressEntity addressEntity = new AddressEntity();

        addressEntity.setFlatBuilNo(saveAddressRequest.getFlatBuildingName());
        addressEntity.setCity(saveAddressRequest.getCity());
        addressEntity.setLocality(saveAddressRequest.getLocality());
        addressEntity.setPincode(saveAddressRequest.getPincode());
        addressEntity.setUuid(UUID.randomUUID().toString());

        StateEntity stateEntity = addressService.getStateByUUID(saveAddressRequest.getStateUuid());

        AddressEntity savedAddress = addressService.saveAddress(addressEntity, stateEntity);

        addressService.saveCustomerAddressEntity(customerEntity, savedAddress);

        SaveAddressResponse saveAddressResponse = new SaveAddressResponse()
                .id(savedAddress.getUuid())
                .status("ADDRESS SUCCESSFULLY REGISTERED");
        return new ResponseEntity<>(saveAddressResponse, HttpStatus.CREATED);
    }

    /**
     * The method handles get all Address  request.It takes the authorization & produces response
     * in AddressListResponse and returns list of Customer Address .If error Return error code and
     * error Message.
     *
     * @param authorization Authorization Details
     * @return List of Addresses
     * @throws AuthorizationFailedException Authorization Failed Exception
     */
    @CrossOrigin
    @GetMapping(path = "/address/customer", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<AddressListResponse> getAllSavedAddress(@RequestHeader("authorization") final String authorization) throws AuthorizationFailedException {
        String accessToken = authorization.split(BEARER_AUTH)[1];

        CustomerEntity customerEntity = customerService.getCustomer(accessToken);

        List<AddressEntity> addressEntities = addressService.getAllAddress(customerEntity);
        Collections.reverse(addressEntities); //Reversing the list to show last saved as first.

        List<AddressList> addressLists = new LinkedList<>();
        addressEntities.forEach(addressEntity -> {
            AddressListState addressListState = new AddressListState()
                    .stateName(addressEntity.getState().getStateName())
                    .id(UUID.fromString(addressEntity.getState().getStateUuid()));
            AddressList addressList = new AddressList()
                    .id(UUID.fromString(addressEntity.getUuid()))
                    .city(addressEntity.getCity())
                    .flatBuildingName(addressEntity.getFlatBuilNo())
                    .locality(addressEntity.getLocality())
                    .pincode(addressEntity.getPincode())
                    .state(addressListState);
            addressLists.add(addressList);
        });
        AddressListResponse addressListResponse = new AddressListResponse().addresses(addressLists);
        return new ResponseEntity<>(addressListResponse, HttpStatus.OK);
    }

    /**
     * The method handles delete  Address  request.It takes the authorization and path variables address UUID
     * & produces response in DeleteAddressResponse and returns UUID of deleted address and Successful message,
     * if error Return error code and error Message.
     *
     * @param authorization Authorization Details
     * @param addressUuid   Address UUID
     * @return Deleted Address
     * @throws AuthorizationFailedException Authorization Failed Exception
     * @throws AddressNotFoundException     Address Not Found Exception
     */
    @CrossOrigin
    @DeleteMapping(path = "/address/{address_id}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<DeleteAddressResponse> deleteSavedAddress(@RequestHeader("authorization") final String authorization, @PathVariable(value = "address_id") final String addressUuid) throws AuthorizationFailedException, AddressNotFoundException {
        String accessToken = authorization.split(BEARER_AUTH)[1];

        CustomerEntity customerEntity = customerService.getCustomer(accessToken);

        AddressEntity addressEntity = addressService.getAddressByUUID(addressUuid, customerEntity);

        AddressEntity deletedAddressEntity = addressService.deleteAddress(addressEntity);

        DeleteAddressResponse deleteAddressResponse = new DeleteAddressResponse()
                .id(UUID.fromString(deletedAddressEntity.getUuid()))
                .status("ADDRESS DELETED SUCCESSFULLY");

        return new ResponseEntity<>(deleteAddressResponse, HttpStatus.OK);
    }

    /**
     * The method handles States request. It produces response in StatesListResponse and returns UUID & stateName,
     * if error Return error code and error Message.
     *
     * @return List of States
     */
    /*  The method handles States request.It produces response in StatesListResponse and returns UUID & stateName .If error Return error code and error Message.
     */
    @CrossOrigin
    @GetMapping(path = "/states", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<StatesListResponse> getAllStates() {
        List<StateEntity> stateEntities = addressService.getAllStates();

        if (!stateEntities.isEmpty()) {
            List<StatesList> statesLists = new LinkedList<>();
            stateEntities.forEach(stateEntity -> {
                StatesList statesList = new StatesList()
                        .id(UUID.fromString(stateEntity.getStateUuid()))
                        .stateName(stateEntity.getStateName());
                statesLists.add(statesList);
            });
            StatesListResponse statesListResponse = new StatesListResponse().states(statesLists);
            return new ResponseEntity<>(statesListResponse, HttpStatus.OK);
        } else
            return new ResponseEntity<>(new StatesListResponse(), HttpStatus.OK);
    }
}
