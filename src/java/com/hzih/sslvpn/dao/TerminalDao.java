package com.hzih.sslvpn.dao;

import com.hzih.sslvpn.domain.Terminal;

/**
 * Created by hhm on 2014/12/17.
 */
public interface TerminalDao {
    public boolean add(Terminal terminal)throws Exception;

    public boolean modify(Terminal terminal)throws Exception;

    public boolean delete(Terminal terminal)throws Exception;

    public Terminal findById(int id)throws Exception;

    Terminal findByName(String t_user_name);
}
