package com.hzih.sslvpn.entity.mapper;

import com.hzih.sslvpn.domain.User;
import com.hzih.sslvpn.entity.X509User;

import javax.naming.directory.Attributes;
import javax.naming.directory.SearchResult;

/**
 * Created with IntelliJ IDEA.
 * User: hhm
 * Date: 14-5-2
 * Time: 下午4:46
 * To change this template use File | Settings | File Templates.
 */
public class X509UserAttributeMapper {

    public static User mapFromAttributes(SearchResult sr) throws javax.naming.NamingException {
        User user = new User();

        Attributes attr = sr.getAttributes();

        user.setCn((String) attr.get(X509User.getCnAttr()).get());

        if (attr.get(X509User.getIdCardAttr()) != null) {
            user.setId_card((String) attr.get(X509User.getIdCardAttr()).get());
        }
        if (attr.get(X509User.getPhoneAttr()) != null) {
            user.setPhone((String) attr.get(X509User.getPhoneAttr()).get());
        }
        if (attr.get(X509User.getAddressAttr()) != null) {
            user.setAddress((String) attr.get(X509User.getAddressAttr()).get());
        }
        if (attr.get(X509User.getUserEmailAttr()) != null) {
            user.setEmail((String) attr.get(X509User.getUserEmailAttr()).get());
        }
        if (attr.get(X509User.getEmployeeCodeAttr()) != null) {
            user.setEmployeeCode((String) attr.get(X509User.getEmployeeCodeAttr()).get());
        }
        if (attr.get(X509User.getOrgcodeAttr()) != null) {
            user.setOrgCode((String)attr.get(X509User.getOrgcodeAttr()).get());
        }
        if (attr.get(X509User.getCertStatusAttr()) != null) {
            user.setStatus((String)attr.get(X509User.getCertStatusAttr()).get());
        }
        if (attr.get(X509User.getSerialAttr()) != null) {
            user.setSerial_number((String)attr.get(X509User.getSerialAttr()).get());
        }
        /* if (attr.get(X509User.getCreateDateAttr()) != null) {
            user.setCreate_Date((String)attr.get(X509User.getCreateDateAttr()).get());
        }
        if (attr.get(X509User.getEndDateAttr()) != null) {
            user.setEnd_Date((String)attr.get(X509User.getEndDateAttr()).get());
        }*/
        if (attr.get(X509User.getIssueCaAttr()) != null) {
            user.setIssueCa((String)attr.get(X509User.getIssueCaAttr()).get());
        }
        if (attr.get(X509User.getProvinceAttr()) != null) {
            user.setProvince((String)attr.get(X509User.getProvinceAttr()).get());
        }
        if (attr.get(X509User.getCityAttr()) != null) {
            user.setCity((String)attr.get(X509User.getCityAttr()).get());
        }
        if (attr.get(X509User.getOrganizationAttr()) != null) {
            user.setOrganization((String)attr.get(X509User.getOrganizationAttr()).get());
        }
        if (attr.get(X509User.getInstitutionAttr()) != null) {
            user.setInstitutions((String)attr.get(X509User.getInstitutionAttr()).get());
        }
        if (attr.get(X509User.getDescAttr()) != null) {
            user.setDescription((String)attr.get(X509User.getDescAttr()).get());
        }
        return user;
    }
}
