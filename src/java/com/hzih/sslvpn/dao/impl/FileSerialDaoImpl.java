package com.hzih.sslvpn.dao.impl;

import cn.collin.commons.dao.MyDaoSupport;
import com.hzih.sslvpn.dao.FileSerialDao;
import com.hzih.sslvpn.domain.FileSerial;
import org.hibernate.Query;
import org.hibernate.Session;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: 13-9-9
 * Time: 下午3:05
 * To change this template use File | Settings | File Templates.
 */
public class FileSerialDaoImpl extends MyDaoSupport implements FileSerialDao{
    @Override
    public void update_serial(String update_flag, int update_num) throws Exception {
        String s ="update FileSerial fileSerial set "+update_flag+"="+update_num+" where fileSerial.id =1 ";
        Session session = super.getSession();
        session.beginTransaction();
        Query query = session.createQuery(s);
        query.executeUpdate();
        session.getTransaction().commit();
        session.close();
    }

    @Override
    public int get_serial(String get_flag) throws Exception {
        String hql="from FileSerial fileSerial where fileSerial.id = 1";
        List<FileSerial> fileSerials  = super.getHibernateTemplate().find(hql);
        if(fileSerials!=null&&fileSerials.size()>0) {
            if(get_flag.equals("ca_serial"))
                return fileSerials.get(0).getCa_serial();
            else if(get_flag.equals("server_serial"))
                return fileSerials.get(0).getServer_serial();
            else if(get_flag.equals("key_serial"))
                return fileSerials.get(0).getKey_serial();
        }
        return 0;
    }

    @Override
    public void setEntityClass() {
        this.entityClass = FileSerial.class;
    }
}
