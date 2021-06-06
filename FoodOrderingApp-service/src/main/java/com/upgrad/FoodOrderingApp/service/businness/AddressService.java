package com.upgrad.FoodOrderingApp.service.businness;

import com.upgrad.FoodOrderingApp.service.common.UtilityProvider;
import com.upgrad.FoodOrderingApp.service.dao.*;
import com.upgrad.FoodOrderingApp.service.entity.*;
import com.upgrad.FoodOrderingApp.service.exception.AddressNotFoundException;
import com.upgrad.FoodOrderingApp.service.exception.AuthorizationFailedException;
import com.upgrad.FoodOrderingApp.service.exception.SaveAddressException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;

/**
 * This Class implements the services for Address Related Request
 */
@Service
public class AddressService {
    @Autowired
    AddressDao addressDao;

    @Autowired
    CustomerAuthDao customerAuthDao;

    @Autowired
    UtilityProvider utilityProvider;

    @Autowired
    StateDao stateDao;

    @Autowired
    CustomerAddressDao customerAddressDao;

    @Autowired
    OrderDao orderDao;

    /**
     * Save Address of the Customer
     *
     * @param addressEntity Address Details
     * @param stateEntity   State Information
     * @return Saved Address
     * @throws SaveAddressException Save Address Exception
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public AddressEntity saveAddress(AddressEntity addressEntity, StateEntity stateEntity) throws SaveAddressException {
        if (addressEntity.getCity() == null || addressEntity.getFlatBuilNo() == null || addressEntity.getPincode() == null || addressEntity.getLocality() == null) {
            throw new SaveAddressException("SAR-001", "No field can be empty");
        }
        if (!utilityProvider.checkIfPincodeIsValid(addressEntity.getPincode())) {
            throw new SaveAddressException("SAR-002", "Invalid pincode");
        }
        addressEntity.setState(stateEntity);
        return addressDao.saveAddress(addressEntity);

    }

    /**
     * Get all Addresses of a Customer
     *
     * @param customerEntity Customer Details
     * @return List of Addresses
     */
    public List<AddressEntity> getAllAddress(CustomerEntity customerEntity) {
        List<AddressEntity> addressEntities = new LinkedList<>();

        List<CustomerAddressEntity> customerAddressEntities = customerAddressDao.getAllCustomerAddressByCustomer(customerEntity);
        if (customerAddressEntities != null) {
            customerAddressEntities.forEach(customerAddressEntity ->
                    addressEntities.add(customerAddressEntity.getAddress()));
        }
        return addressEntities;
    }

    /**
     * Get State by UUID
     *
     * @param uuid UUID
     * @return State Information
     * @throws AddressNotFoundException Address Not Found Exception
     */
    public StateEntity getStateByUUID(String uuid) throws AddressNotFoundException {
        StateEntity stateEntity = stateDao.getStateByUuid(uuid);
        if (stateEntity == null) {
            throw new AddressNotFoundException("ANF-002", "No state by this id");
        }
        return stateEntity;
    }

    /**
     * Save Customer Address Entity
     *
     * @param customerEntity Customer Details
     * @param addressEntity  Customer Address
     * @return Saved CustomerAddressEntity
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public CustomerAddressEntity saveCustomerAddressEntity(CustomerEntity customerEntity, AddressEntity addressEntity) {
        CustomerAddressEntity customerAddressEntity = new CustomerAddressEntity();
        customerAddressEntity.setCustomer(customerEntity);
        customerAddressEntity.setAddress(addressEntity);

        return customerAddressDao.saveCustomerAddress(customerAddressEntity);

    }

    /**
     * Get Address by UUID
     *
     * @param addressUuid    UUID of the Address
     * @param customerEntity Customer Details
     * @return AddressEntity
     * @throws AuthorizationFailedException Authorization Failed Exception
     * @throws AddressNotFoundException     Address Not Found Exception
     */

    @Transactional(propagation = Propagation.REQUIRED)
    public AddressEntity getAddressByUUID(String addressUuid, CustomerEntity customerEntity) throws AuthorizationFailedException, AddressNotFoundException {
        if (StringUtils.isEmpty(addressUuid)) {
            throw new AddressNotFoundException("ANF-005", "Address id can not be empty");
        }

        AddressEntity addressEntity = addressDao.getAddressByUuid(addressUuid);
        if (addressEntity == null) {
            throw new AddressNotFoundException("ANF-003", "No address by this id");
        }

        CustomerAddressEntity customerAddressEntity = customerAddressDao.getCustomerAddressByAddress(addressEntity);

        if (customerAddressEntity == null ||
                !customerAddressEntity.getCustomer().getUuid().equals(customerEntity.getUuid()))
            throw new AuthorizationFailedException("ATHR-004", "You are not authorized to view/update/delete any one else's address");

        return addressEntity;

    }

    /**
     * Get all State
     *
     * @return StateEntity
     */
    public List<StateEntity> getAllStates() {
        return stateDao.getAllStates();

    }

    /**
     * Delete Address of a Customer
     *
     * @param addressEntity Address to be Deleted
     * @return Deleted Address
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public AddressEntity deleteAddress(AddressEntity addressEntity) {
        List<OrdersEntity> ordersEntities = orderDao.getOrdersByAddress(addressEntity);

        if (ordersEntities == null || ordersEntities.isEmpty()) {
            return addressDao.deleteAddress(addressEntity);

        } else {
            addressEntity.setActive(0);
            return addressDao.updateAddressActiveStatus(addressEntity);

        }
    }
}
