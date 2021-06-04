package com.cwk.crm.workbench.service.Impl;

import com.cwk.crm.utils.DateTimeUtil;
import com.cwk.crm.utils.SqlSessionUtil;
import com.cwk.crm.utils.UUIDUtil;
import com.cwk.crm.vo.PaginationVO;
import com.cwk.crm.workbench.dao.*;
import com.cwk.crm.workbench.domain.*;
import com.cwk.crm.workbench.service.ClueService;
import com.fasterxml.jackson.core.JsonToken;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ClueServiceImpl implements ClueService {
    private ClueDao clueDao = SqlSessionUtil.getSqlSession().getMapper(ClueDao.class);
    private ClueRemarkDao clueRemarkDao = SqlSessionUtil.getSqlSession().getMapper(ClueRemarkDao.class);
    private ClueActivityRelationDao clueActivityRelationDao = SqlSessionUtil.getSqlSession().getMapper(ClueActivityRelationDao.class);

    private CustomerDao customerDao = SqlSessionUtil.getSqlSession().getMapper(CustomerDao.class);
    private CustomerRemarkDao customerRemarkDao = SqlSessionUtil.getSqlSession().getMapper(CustomerRemarkDao.class);

    private ContactsDao contactsDao = SqlSessionUtil.getSqlSession().getMapper(ContactsDao.class);
    private ContactsRemarkDao contactsRemarkDao = SqlSessionUtil.getSqlSession().getMapper(ContactsRemarkDao.class);
    private ContactsActivityRelationDao contactsActivityRelationDao = SqlSessionUtil.getSqlSession().getMapper(ContactsActivityRelationDao.class);

    private TranDao tranDao = SqlSessionUtil.getSqlSession().getMapper(TranDao.class);
    private TranHistoryDao tranHistoryDao = SqlSessionUtil.getSqlSession().getMapper(TranHistoryDao.class);

    @Override
    public boolean save(Clue c) {
        boolean success = true;
        int count = clueDao.save(c);
        if(count != 1){
            success = false;
        }
        return success;
    }

    @Override
    public PaginationVO<Clue> pageList(Map<String, Object> map) {

        int total = clueDao.getTotalByCondition(map);
        List<Clue> dataList = clueDao.getClueListByCondition(map);
        PaginationVO<Clue> vo = new PaginationVO<Clue>();
        vo.setTotal(total);
        vo.setDataList(dataList);
        return vo;

    }

    @Override
    public Clue detail(String id) {
        Clue c = clueDao.detail(id);
        return c;
    }

    @Override
    public boolean unbundById(String id) {
        boolean success = true;
        int count = clueActivityRelationDao.unbundById(id);
        if(count != 1){
            success = false;
        }
        return success;
    }

    @Override
    public boolean bund(String cid, String[] aids) {
        boolean success = true;
        List<ClueActivityRelation> carList = new ArrayList<ClueActivityRelation>();
        for(String aid:aids){
            ClueActivityRelation car = new ClueActivityRelation();
            car.setId(UUIDUtil.getUUID());
            car.setClueId(cid);
            car.setActivityId(aid);
            carList.add(car);
        }
        int count = clueActivityRelationDao.bund(carList);
        if(count != aids.length){
            success = false;
        }
        return success;
    }

    @Override
    public boolean convert(String clueId, Tran t, String createBy) {
        String createTime = DateTimeUtil.getSysTime();
        boolean flag = true;
        //(1) 获取到线索id，通过线索id获取线索对象（线索对象当中封装了线索的信息）
        Clue clue = clueDao.getById(clueId);
        //(2) 通过线索对象提取客户信息，当该客户不存在的时候，新建客户（根据公司的名称精确匹配，判断该客户是否存在！）
        String company = clue.getCompany();
        Customer customer = customerDao.getCustomerByName(company);
        if(customer == null){
            customer = new Customer();
            customer.setId(UUIDUtil.getUUID());
            customer.setAddress(clue.getAddress());
            customer.setWebsite(clue.getWebsite());
            customer.setPhone(clue.getPhone());
            customer.setOwner(clue.getOwner());
            customer.setNextContactTime(clue.getNextContactTime());
            customer.setName(company);
            customer.setDescription(clue.getDescription());
            customer.setCreateTime(createTime);
            customer.setCreateBy(createBy);
            customer.setContactSummary(clue.getContactSummary());
            //添加客户
            int count = customerDao.save(customer);
            if(count != 1){
                flag = false;
            }
        }
        //(3) 通过线索对象提取联系人信息，保存联系人
        Contacts contacts = new Contacts();
        contacts.setId(UUIDUtil.getUUID());
        contacts.setSource(clue.getSource());
        contacts.setOwner(clue.getOwner());
        contacts.setNextContactTime(clue.getNextContactTime());
        contacts.setMphone(clue.getMphone());
        contacts.setJob(clue.getJob());
        contacts.setFullname(clue.getFullname());
        contacts.setEmail(clue.getEmail());
        contacts.setDescription(clue.getDescription());
        contacts.setCustomerId(customer.getId());
        contacts.setCreateTime(createTime);
        contacts.setCreateBy(createBy);
        contacts.setContactSummary(clue.getContactSummary());
        contacts.setAppellation(clue.getAppellation());
        contacts.setAddress(clue.getAddress());
        //添加联系人
        int count1 = contactsDao.save(contacts);
        if(count1 != 1){
            flag = false;
        }
        //(4) 线索备注转换到客户备注以及联系人备注
        List<ClueRemark> clueRemarkList = clueRemarkDao.getListByClueId(clueId);
        //取出每一条线索的备注
        for(ClueRemark clueRemark:clueRemarkList){
            //取出备注信息（主要转换到客户备注和联系人备注的就是这条信息）
            String noteContent = clueRemark.getNoteContent();

            //创建客户备注对象，添加客户备注
            CustomerRemark customerRemark = new CustomerRemark();
            customerRemark.setId(UUIDUtil.getUUID());
            customerRemark.setCreateBy(createBy);
            customerRemark.setCreateTime(createTime);
            customerRemark.setCustomerId(customer.getId());
            customerRemark.setEditFlag("0");
            customerRemark.setNoteContent(noteContent);
            int count2 = customerRemarkDao.save(customerRemark);
            if(count2 != 1){
                flag = false;
            }

            //创建联系人备注对象，添加联系人备注
            ContactsRemark contactsRemark = new ContactsRemark();
            contactsRemark.setId(UUIDUtil.getUUID());
            contactsRemark.setCreateBy(createBy);
            contactsRemark.setCreateTime(createTime);
            contactsRemark.setContactsId(contacts.getId());
            contactsRemark.setEditFlag("0");
            contactsRemark.setNoteContent(noteContent);
            int count3 = contactsRemarkDao.save(contactsRemark);
            if(count3 != 1){
                flag = false;
            }
        }

        //(5) “线索和市场活动”的关系转换到“联系人和市场活动”的关系
        List<ClueActivityRelation> clueActivityRelationList = clueActivityRelationDao.getListByClueId(clueId);
        for(ClueActivityRelation clueActivityRelation:clueActivityRelationList){
            String activityId = clueActivityRelation.getActivityId();
            ContactsActivityRelation contactsActivityRelation = new ContactsActivityRelation();
            contactsActivityRelation.setId(UUIDUtil.getUUID());
            contactsActivityRelation.setContactsId(contacts.getId());
            contactsActivityRelation.setActivityId(activityId);
            int count4 = contactsActivityRelationDao.save(contactsActivityRelation);
            if(count4 != 1){
                flag = false;
            }
        }

        //(6) 如果有创建交易需求，创建一条交易
        if(t != null){
            t.setSource(clue.getSource());
            t.setOwner(clue.getOwner());
            t.setNextContactTime(clue.getNextContactTime());
            t.setDescription(clue.getDescription());
            t.setCustomerId(customer.getId());
            t.setContactSummary(clue.getContactSummary());
            t.setContactsId(contacts.getId());
            int count5 = tranDao.save(t);
            if(count5 != 1){
                flag = false;
            }
            //(7) 如果创建了交易，则创建一条该交易下的交易历史
            TranHistory tranHistory = new TranHistory();
            tranHistory.setId(UUIDUtil.getUUID());
            tranHistory.setMoney(t.getMoney());
            tranHistory.setStage(t.getStage());
            tranHistory.setCreateBy(createBy);
            tranHistory.setCreateTime(createTime);
            tranHistory.setExpectedDate(t.getExpectedDate());
            tranHistory.setTranId(t.getId());
            int count6 = tranHistoryDao.save(tranHistory);
            if(count6 != 1){
                flag = false;
            }
        }

        //(8) 删除线索备注
        int count7 = clueRemarkDao.delete(clueId);
        if(count7 != 1){
            flag = false;
        }
        //(9) 删除线索和市场活动的关系
        int count8 = clueActivityRelationDao.delete(clueId);
        if(count8 != clueActivityRelationList.size()){
            flag = false;
        }
        //(10) 删除线索
        int count9 = clueDao.delete(clueId);
        if(count9 != 1){
            flag = false;
        }

        return flag;
    }


}
