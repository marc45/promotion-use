package com.edu.sky.promotion.po.example;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CouponExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public CouponExample() {
        oredCriteria = new ArrayList<Criteria>();
    }

    public void setOrderByClause(String orderByClause) {
        this.orderByClause = orderByClause;
    }

    public String getOrderByClause() {
        return orderByClause;
    }

    public void setDistinct(boolean distinct) {
        this.distinct = distinct;
    }

    public boolean isDistinct() {
        return distinct;
    }

    public List<Criteria> getOredCriteria() {
        return oredCriteria;
    }

    public void or(Criteria criteria) {
        oredCriteria.add(criteria);
    }

    public Criteria or() {
        Criteria criteria = createCriteriaInternal();
        oredCriteria.add(criteria);
        return criteria;
    }

    public Criteria createCriteria() {
        Criteria criteria = createCriteriaInternal();
        if (oredCriteria.size() == 0) {
            oredCriteria.add(criteria);
        }
        return criteria;
    }

    protected Criteria createCriteriaInternal() {
        Criteria criteria = new Criteria();
        return criteria;
    }

    public void clear() {
        oredCriteria.clear();
        orderByClause = null;
        distinct = false;
    }

    protected abstract static class GeneratedCriteria {
        protected List<Criterion> criteria;

        protected GeneratedCriteria() {
            super();
            criteria = new ArrayList<Criterion>();
        }

        public boolean isValid() {
            return criteria.size() > 0;
        }

        public List<Criterion> getAllCriteria() {
            return criteria;
        }

        public List<Criterion> getCriteria() {
            return criteria;
        }

        protected void addCriterion(String condition) {
            if (condition == null) {
                throw new RuntimeException("Value for condition cannot be null");
            }
            criteria.add(new Criterion(condition));
        }

        protected void addCriterion(String condition, Object value, String property) {
            if (value == null) {
                throw new RuntimeException("Value for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value));
        }

        protected void addCriterion(String condition, Object value1, Object value2, String property) {
            if (value1 == null || value2 == null) {
                throw new RuntimeException("Between values for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value1, value2));
        }

        public Criteria andIdIsNull() {
            addCriterion("id is null");
            return (Criteria) this;
        }

        public Criteria andIdIsNotNull() {
            addCriterion("id is not null");
            return (Criteria) this;
        }

        public Criteria andIdEqualTo(Long value) {
            addCriterion("id =", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotEqualTo(Long value) {
            addCriterion("id <>", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThan(Long value) {
            addCriterion("id >", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThanOrEqualTo(Long value) {
            addCriterion("id >=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThan(Long value) {
            addCriterion("id <", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThanOrEqualTo(Long value) {
            addCriterion("id <=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdIn(List<Long> values) {
            addCriterion("id in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotIn(List<Long> values) {
            addCriterion("id not in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdBetween(Long value1, Long value2) {
            addCriterion("id between", value1, value2, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotBetween(Long value1, Long value2) {
            addCriterion("id not between", value1, value2, "id");
            return (Criteria) this;
        }

        public Criteria andNameIsNull() {
            addCriterion("name is null");
            return (Criteria) this;
        }

        public Criteria andNameIsNotNull() {
            addCriterion("name is not null");
            return (Criteria) this;
        }

        public Criteria andNameEqualTo(String value) {
            addCriterion("name =", value, "name");
            return (Criteria) this;
        }

        public Criteria andNameNotEqualTo(String value) {
            addCriterion("name <>", value, "name");
            return (Criteria) this;
        }

        public Criteria andNameGreaterThan(String value) {
            addCriterion("name >", value, "name");
            return (Criteria) this;
        }

        public Criteria andNameGreaterThanOrEqualTo(String value) {
            addCriterion("name >=", value, "name");
            return (Criteria) this;
        }

        public Criteria andNameLessThan(String value) {
            addCriterion("name <", value, "name");
            return (Criteria) this;
        }

        public Criteria andNameLessThanOrEqualTo(String value) {
            addCriterion("name <=", value, "name");
            return (Criteria) this;
        }

        public Criteria andNameLike(String value) {
            addCriterion("name like", value, "name");
            return (Criteria) this;
        }

        public Criteria andNameNotLike(String value) {
            addCriterion("name not like", value, "name");
            return (Criteria) this;
        }

        public Criteria andNameIn(List<String> values) {
            addCriterion("name in", values, "name");
            return (Criteria) this;
        }

        public Criteria andNameNotIn(List<String> values) {
            addCriterion("name not in", values, "name");
            return (Criteria) this;
        }

        public Criteria andNameBetween(String value1, String value2) {
            addCriterion("name between", value1, value2, "name");
            return (Criteria) this;
        }

        public Criteria andNameNotBetween(String value1, String value2) {
            addCriterion("name not between", value1, value2, "name");
            return (Criteria) this;
        }

        public Criteria andTypeIsNull() {
            addCriterion("type is null");
            return (Criteria) this;
        }

        public Criteria andTypeIsNotNull() {
            addCriterion("type is not null");
            return (Criteria) this;
        }

        public Criteria andTypeEqualTo(Byte value) {
            addCriterion("type =", value, "type");
            return (Criteria) this;
        }

        public Criteria andTypeNotEqualTo(Byte value) {
            addCriterion("type <>", value, "type");
            return (Criteria) this;
        }

        public Criteria andTypeGreaterThan(Byte value) {
            addCriterion("type >", value, "type");
            return (Criteria) this;
        }

        public Criteria andTypeGreaterThanOrEqualTo(Byte value) {
            addCriterion("type >=", value, "type");
            return (Criteria) this;
        }

        public Criteria andTypeLessThan(Byte value) {
            addCriterion("type <", value, "type");
            return (Criteria) this;
        }

        public Criteria andTypeLessThanOrEqualTo(Byte value) {
            addCriterion("type <=", value, "type");
            return (Criteria) this;
        }

        public Criteria andTypeIn(List<Byte> values) {
            addCriterion("type in", values, "type");
            return (Criteria) this;
        }

        public Criteria andTypeNotIn(List<Byte> values) {
            addCriterion("type not in", values, "type");
            return (Criteria) this;
        }

        public Criteria andTypeBetween(Byte value1, Byte value2) {
            addCriterion("type between", value1, value2, "type");
            return (Criteria) this;
        }

        public Criteria andTypeNotBetween(Byte value1, Byte value2) {
            addCriterion("type not between", value1, value2, "type");
            return (Criteria) this;
        }

        public Criteria andFaceValueIsNull() {
            addCriterion("face_value is null");
            return (Criteria) this;
        }

        public Criteria andFaceValueIsNotNull() {
            addCriterion("face_value is not null");
            return (Criteria) this;
        }

        public Criteria andFaceValueEqualTo(Double value) {
            addCriterion("face_value =", value, "faceValue");
            return (Criteria) this;
        }

        public Criteria andFaceValueNotEqualTo(Double value) {
            addCriterion("face_value <>", value, "faceValue");
            return (Criteria) this;
        }

        public Criteria andFaceValueGreaterThan(Double value) {
            addCriterion("face_value >", value, "faceValue");
            return (Criteria) this;
        }

        public Criteria andFaceValueGreaterThanOrEqualTo(Double value) {
            addCriterion("face_value >=", value, "faceValue");
            return (Criteria) this;
        }

        public Criteria andFaceValueLessThan(Double value) {
            addCriterion("face_value <", value, "faceValue");
            return (Criteria) this;
        }

        public Criteria andFaceValueLessThanOrEqualTo(Double value) {
            addCriterion("face_value <=", value, "faceValue");
            return (Criteria) this;
        }

        public Criteria andFaceValueIn(List<Double> values) {
            addCriterion("face_value in", values, "faceValue");
            return (Criteria) this;
        }

        public Criteria andFaceValueNotIn(List<Double> values) {
            addCriterion("face_value not in", values, "faceValue");
            return (Criteria) this;
        }

        public Criteria andFaceValueBetween(Double value1, Double value2) {
            addCriterion("face_value between", value1, value2, "faceValue");
            return (Criteria) this;
        }

        public Criteria andFaceValueNotBetween(Double value1, Double value2) {
            addCriterion("face_value not between", value1, value2, "faceValue");
            return (Criteria) this;
        }

        public Criteria andInventoryFlagIsNull() {
            addCriterion("inventory_flag is null");
            return (Criteria) this;
        }

        public Criteria andInventoryFlagIsNotNull() {
            addCriterion("inventory_flag is not null");
            return (Criteria) this;
        }

        public Criteria andInventoryFlagEqualTo(Boolean value) {
            addCriterion("inventory_flag =", value, "inventoryFlag");
            return (Criteria) this;
        }

        public Criteria andInventoryFlagNotEqualTo(Boolean value) {
            addCriterion("inventory_flag <>", value, "inventoryFlag");
            return (Criteria) this;
        }

        public Criteria andInventoryFlagGreaterThan(Boolean value) {
            addCriterion("inventory_flag >", value, "inventoryFlag");
            return (Criteria) this;
        }

        public Criteria andInventoryFlagGreaterThanOrEqualTo(Boolean value) {
            addCriterion("inventory_flag >=", value, "inventoryFlag");
            return (Criteria) this;
        }

        public Criteria andInventoryFlagLessThan(Boolean value) {
            addCriterion("inventory_flag <", value, "inventoryFlag");
            return (Criteria) this;
        }

        public Criteria andInventoryFlagLessThanOrEqualTo(Boolean value) {
            addCriterion("inventory_flag <=", value, "inventoryFlag");
            return (Criteria) this;
        }

        public Criteria andInventoryFlagIn(List<Boolean> values) {
            addCriterion("inventory_flag in", values, "inventoryFlag");
            return (Criteria) this;
        }

        public Criteria andInventoryFlagNotIn(List<Boolean> values) {
            addCriterion("inventory_flag not in", values, "inventoryFlag");
            return (Criteria) this;
        }

        public Criteria andInventoryFlagBetween(Boolean value1, Boolean value2) {
            addCriterion("inventory_flag between", value1, value2, "inventoryFlag");
            return (Criteria) this;
        }

        public Criteria andInventoryFlagNotBetween(Boolean value1, Boolean value2) {
            addCriterion("inventory_flag not between", value1, value2, "inventoryFlag");
            return (Criteria) this;
        }

        public Criteria andRestrictFlagIsNull() {
            addCriterion("restrict_flag is null");
            return (Criteria) this;
        }

        public Criteria andRestrictFlagIsNotNull() {
            addCriterion("restrict_flag is not null");
            return (Criteria) this;
        }

        public Criteria andRestrictFlagEqualTo(Boolean value) {
            addCriterion("restrict_flag =", value, "restrictFlag");
            return (Criteria) this;
        }
        public Criteria andDelFlagEqualTo(Boolean value) {
            addCriterion("del_flag =", value, "delFlag");
            return (Criteria) this;
        }
        public Criteria andHomeShowEqualTo(Boolean value) {
            addCriterion("home_show =", value, "homeFlag");
            return (Criteria) this;
        }
        public Criteria andRepeatFlagEqualTo(Boolean value) {
            addCriterion("repeat_flag =", value, "repeatFlag");
            return (Criteria) this;
        }

        public Criteria andRestrictFlagNotEqualTo(Boolean value) {
            addCriterion("restrict_flag <>", value, "restrictFlag");
            return (Criteria) this;
        }

        public Criteria andRestrictFlagGreaterThan(Boolean value) {
            addCriterion("restrict_flag >", value, "restrictFlag");
            return (Criteria) this;
        }

        public Criteria andRestrictFlagGreaterThanOrEqualTo(Boolean value) {
            addCriterion("restrict_flag >=", value, "restrictFlag");
            return (Criteria) this;
        }

        public Criteria andRestrictFlagLessThan(Boolean value) {
            addCriterion("restrict_flag <", value, "restrictFlag");
            return (Criteria) this;
        }

        public Criteria andRestrictFlagLessThanOrEqualTo(Boolean value) {
            addCriterion("restrict_flag <=", value, "restrictFlag");
            return (Criteria) this;
        }

        public Criteria andRestrictFlagIn(List<Boolean> values) {
            addCriterion("restrict_flag in", values, "restrictFlag");
            return (Criteria) this;
        }

        public Criteria andRestrictFlagNotIn(List<Boolean> values) {
            addCriterion("restrict_flag not in", values, "restrictFlag");
            return (Criteria) this;
        }

        public Criteria andRestrictFlagBetween(Boolean value1, Boolean value2) {
            addCriterion("restrict_flag between", value1, value2, "restrictFlag");
            return (Criteria) this;
        }

        public Criteria andRestrictFlagNotBetween(Boolean value1, Boolean value2) {
            addCriterion("restrict_flag not between", value1, value2, "restrictFlag");
            return (Criteria) this;
        }

        public Criteria andApplicationTypeIsNull() {
            addCriterion("application_type is null");
            return (Criteria) this;
        }

        public Criteria andApplicationTypeIsNotNull() {
            addCriterion("application_type is not null");
            return (Criteria) this;
        }

        public Criteria andApplicationTypeEqualTo(Byte value) {
            addCriterion("application_type =", value, "applicationType");
            return (Criteria) this;
        }

        public Criteria andApplicationTypeNotEqualTo(Byte value) {
            addCriterion("application_type <>", value, "applicationType");
            return (Criteria) this;
        }

        public Criteria andApplicationTypeGreaterThan(Byte value) {
            addCriterion("application_type >", value, "applicationType");
            return (Criteria) this;
        }

        public Criteria andApplicationTypeGreaterThanOrEqualTo(Byte value) {
            addCriterion("application_type >=", value, "applicationType");
            return (Criteria) this;
        }

        public Criteria andApplicationTypeLessThan(Byte value) {
            addCriterion("application_type <", value, "applicationType");
            return (Criteria) this;
        }

        public Criteria andApplicationTypeLessThanOrEqualTo(Byte value) {
            addCriterion("application_type <=", value, "applicationType");
            return (Criteria) this;
        }

        public Criteria andApplicationTypeIn(List<Byte> values) {
            addCriterion("application_type in", values, "applicationType");
            return (Criteria) this;
        }

        public Criteria andApplicationTypeNotIn(List<Byte> values) {
            addCriterion("application_type not in", values, "applicationType");
            return (Criteria) this;
        }

        public Criteria andApplicationTypeBetween(Byte value1, Byte value2) {
            addCriterion("application_type between", value1, value2, "applicationType");
            return (Criteria) this;
        }

        public Criteria andApplicationTypeNotBetween(Byte value1, Byte value2) {
            addCriterion("application_type not between", value1, value2, "applicationType");
            return (Criteria) this;
        }

        public Criteria andFixTypeIsNull() {
            addCriterion("fix_type is null");
            return (Criteria) this;
        }

        public Criteria andFixTypeIsNotNull() {
            addCriterion("fix_type is not null");
            return (Criteria) this;
        }

        public Criteria andFixTypeEqualTo(Byte value) {
            addCriterion("fix_type =", value, "fixType");
            return (Criteria) this;
        }

        public Criteria andFixTypeNotEqualTo(Byte value) {
            addCriterion("fix_type <>", value, "fixType");
            return (Criteria) this;
        }

        public Criteria andFixTypeGreaterThan(Byte value) {
            addCriterion("fix_type >", value, "fixType");
            return (Criteria) this;
        }

        public Criteria andFixTypeGreaterThanOrEqualTo(Byte value) {
            addCriterion("fix_type >=", value, "fixType");
            return (Criteria) this;
        }

        public Criteria andFixTypeLessThan(Byte value) {
            addCriterion("fix_type <", value, "fixType");
            return (Criteria) this;
        }

        public Criteria andFixTypeLessThanOrEqualTo(Byte value) {
            addCriterion("fix_type <=", value, "fixType");
            return (Criteria) this;
        }

        public Criteria andFixTypeIn(List<Byte> values) {
            addCriterion("fix_type in", values, "fixType");
            return (Criteria) this;
        }

        public Criteria andFixTypeNotIn(List<Byte> values) {
            addCriterion("fix_type not in", values, "fixType");
            return (Criteria) this;
        }

        public Criteria andFixTypeBetween(Byte value1, Byte value2) {
            addCriterion("fix_type between", value1, value2, "fixType");
            return (Criteria) this;
        }

        public Criteria andFixTypeNotBetween(Byte value1, Byte value2) {
            addCriterion("fix_type not between", value1, value2, "fixType");
            return (Criteria) this;
        }

        public Criteria andStartTimeIsNull() {
            addCriterion("start_time is null");
            return (Criteria) this;
        }

        public Criteria andStartTimeIsNotNull() {
            addCriterion("start_time is not null");
            return (Criteria) this;
        }

        public Criteria andStartTimeEqualTo(Date value) {
            addCriterion("start_time =", value, "startTime");
            return (Criteria) this;
        }

        public Criteria andStartTimeNotEqualTo(Date value) {
            addCriterion("start_time <>", value, "startTime");
            return (Criteria) this;
        }

        public Criteria andStartTimeGreaterThan(Date value) {
            addCriterion("start_time >", value, "startTime");
            return (Criteria) this;
        }

        public Criteria andStartTimeGreaterThanOrEqualTo(Date value) {
            addCriterion("start_time >=", value, "startTime");
            return (Criteria) this;
        }

        public Criteria andStartTimeLessThan(Date value) {
            addCriterion("start_time <", value, "startTime");
            return (Criteria) this;
        }

        public Criteria andStartTimeLessThanOrEqualTo(Date value) {
            addCriterion("start_time <=", value, "startTime");
            return (Criteria) this;
        }

        public Criteria andStartTimeIn(List<Date> values) {
            addCriterion("start_time in", values, "startTime");
            return (Criteria) this;
        }

        public Criteria andStartTimeNotIn(List<Date> values) {
            addCriterion("start_time not in", values, "startTime");
            return (Criteria) this;
        }

        public Criteria andStartTimeBetween(Date value1, Date value2) {
            addCriterion("start_time between", value1, value2, "startTime");
            return (Criteria) this;
        }

        public Criteria andStartTimeNotBetween(Date value1, Date value2) {
            addCriterion("start_time not between", value1, value2, "startTime");
            return (Criteria) this;
        }

        public Criteria andEndTimeIsNull() {
            addCriterion("end_time is null");
            return (Criteria) this;
        }

        public Criteria andEndTimeIsNotNull() {
            addCriterion("end_time is not null");
            return (Criteria) this;
        }

        public Criteria andEndTimeEqualTo(Date value) {
            addCriterion("end_time =", value, "endTime");
            return (Criteria) this;
        }

        public Criteria andEndTimeNotEqualTo(Date value) {
            addCriterion("end_time <>", value, "endTime");
            return (Criteria) this;
        }

        public Criteria andEndTimeGreaterThan(Date value) {
            addCriterion("end_time >", value, "endTime");
            return (Criteria) this;
        }

        public Criteria andEndTimeGreaterThanOrEqualTo(Date value) {
            addCriterion("end_time >=", value, "endTime");
            return (Criteria) this;
        }

        public Criteria andEndTimeLessThan(Date value) {
            addCriterion("end_time <", value, "endTime");
            return (Criteria) this;
        }

        public Criteria andEndTimeLessThanOrEqualTo(Date value) {
            addCriterion("end_time <=", value, "endTime");
            return (Criteria) this;
        }

        public Criteria andEndTimeIn(List<Date> values) {
            addCriterion("end_time in", values, "endTime");
            return (Criteria) this;
        }

        public Criteria andEndTimeNotIn(List<Date> values) {
            addCriterion("end_time not in", values, "endTime");
            return (Criteria) this;
        }

        public Criteria andEndTimeBetween(Date value1, Date value2) {
            addCriterion("end_time between", value1, value2, "endTime");
            return (Criteria) this;
        }

        public Criteria andEndTimeNotBetween(Date value1, Date value2) {
            addCriterion("end_time not between", value1, value2, "endTime");
            return (Criteria) this;
        }

        public Criteria andExpireDayIsNull() {
            addCriterion("expire_day is null");
            return (Criteria) this;
        }

        public Criteria andExpireDayIsNotNull() {
            addCriterion("expire_day is not null");
            return (Criteria) this;
        }

        public Criteria andExpireDayEqualTo(Short value) {
            addCriterion("expire_day =", value, "expireDay");
            return (Criteria) this;
        }

        public Criteria andExpireDayNotEqualTo(Short value) {
            addCriterion("expire_day <>", value, "expireDay");
            return (Criteria) this;
        }

        public Criteria andExpireDayGreaterThan(Short value) {
            addCriterion("expire_day >", value, "expireDay");
            return (Criteria) this;
        }

        public Criteria andExpireDayGreaterThanOrEqualTo(Short value) {
            addCriterion("expire_day >=", value, "expireDay");
            return (Criteria) this;
        }

        public Criteria andExpireDayLessThan(Short value) {
            addCriterion("expire_day <", value, "expireDay");
            return (Criteria) this;
        }

        public Criteria andExpireDayLessThanOrEqualTo(Short value) {
            addCriterion("expire_day <=", value, "expireDay");
            return (Criteria) this;
        }

        public Criteria andExpireDayIn(List<Short> values) {
            addCriterion("expire_day in", values, "expireDay");
            return (Criteria) this;
        }

        public Criteria andExpireDayNotIn(List<Short> values) {
            addCriterion("expire_day not in", values, "expireDay");
            return (Criteria) this;
        }

        public Criteria andExpireDayBetween(Short value1, Short value2) {
            addCriterion("expire_day between", value1, value2, "expireDay");
            return (Criteria) this;
        }

        public Criteria andExpireDayNotBetween(Short value1, Short value2) {
            addCriterion("expire_day not between", value1, value2, "expireDay");
            return (Criteria) this;
        }

        public Criteria andDescriptionIsNull() {
            addCriterion("description is null");
            return (Criteria) this;
        }

        public Criteria andDescriptionIsNotNull() {
            addCriterion("description is not null");
            return (Criteria) this;
        }

        public Criteria andDescriptionEqualTo(String value) {
            addCriterion("description =", value, "description");
            return (Criteria) this;
        }

        public Criteria andDescriptionNotEqualTo(String value) {
            addCriterion("description <>", value, "description");
            return (Criteria) this;
        }

        public Criteria andDescriptionGreaterThan(String value) {
            addCriterion("description >", value, "description");
            return (Criteria) this;
        }

        public Criteria andDescriptionGreaterThanOrEqualTo(String value) {
            addCriterion("description >=", value, "description");
            return (Criteria) this;
        }

        public Criteria andDescriptionLessThan(String value) {
            addCriterion("description <", value, "description");
            return (Criteria) this;
        }

        public Criteria andDescriptionLessThanOrEqualTo(String value) {
            addCriterion("description <=", value, "description");
            return (Criteria) this;
        }

        public Criteria andDescriptionLike(String value) {
            addCriterion("description like", value, "description");
            return (Criteria) this;
        }

        public Criteria andDescriptionNotLike(String value) {
            addCriterion("description not like", value, "description");
            return (Criteria) this;
        }

        public Criteria andDescriptionIn(List<String> values) {
            addCriterion("description in", values, "description");
            return (Criteria) this;
        }

        public Criteria andDescriptionNotIn(List<String> values) {
            addCriterion("description not in", values, "description");
            return (Criteria) this;
        }

        public Criteria andDescriptionBetween(String value1, String value2) {
            addCriterion("description between", value1, value2, "description");
            return (Criteria) this;
        }

        public Criteria andDescriptionNotBetween(String value1, String value2) {
            addCriterion("description not between", value1, value2, "description");
            return (Criteria) this;
        }

        public Criteria andCommonStateIsNull() {
            addCriterion("common_state is null");
            return (Criteria) this;
        }

        public Criteria andCommonStateIsNotNull() {
            addCriterion("common_state is not null");
            return (Criteria) this;
        }

        public Criteria andCommonStateEqualTo(Byte value) {
            addCriterion("common_state =", value, "commonState");
            return (Criteria) this;
        }

        public Criteria andCommonStateNotEqualTo(Byte value) {
            addCriterion("common_state <>", value, "commonState");
            return (Criteria) this;
        }

        public Criteria andCommonStateGreaterThan(Byte value) {
            addCriterion("common_state >", value, "commonState");
            return (Criteria) this;
        }

        public Criteria andCommonStateGreaterThanOrEqualTo(Byte value) {
            addCriterion("common_state >=", value, "commonState");
            return (Criteria) this;
        }

        public Criteria andCommonStateLessThan(Byte value) {
            addCriterion("common_state <", value, "commonState");
            return (Criteria) this;
        }

        public Criteria andCommonStateLessThanOrEqualTo(Byte value) {
            addCriterion("common_state <=", value, "commonState");
            return (Criteria) this;
        }

        public Criteria andCommonStateIn(List<Byte> values) {
            addCriterion("common_state in", values, "commonState");
            return (Criteria) this;
        }

        public Criteria andCommonStateNotIn(List<Byte> values) {
            addCriterion("common_state not in", values, "commonState");
            return (Criteria) this;
        }

        public Criteria andCommonStateBetween(Byte value1, Byte value2) {
            addCriterion("common_state between", value1, value2, "commonState");
            return (Criteria) this;
        }

        public Criteria andCommonStateNotBetween(Byte value1, Byte value2) {
            addCriterion("common_state not between", value1, value2, "commonState");
            return (Criteria) this;
        }

        public Criteria andOnLineFlagIsNull() {
            addCriterion("on_line_flag is null");
            return (Criteria) this;
        }

        public Criteria andOnLineFlagIsNotNull() {
            addCriterion("on_line_flag is not null");
            return (Criteria) this;
        }

        public Criteria andOnLineFlagEqualTo(Byte value) {
            addCriterion("on_line_flag =", value, "onLineFlag");
            return (Criteria) this;
        }

        public Criteria andOnLineFlagNotEqualTo(Byte value) {
            addCriterion("on_line_flag <>", value, "onLineFlag");
            return (Criteria) this;
        }

        public Criteria andOnLineFlagGreaterThan(Byte value) {
            addCriterion("on_line_flag >", value, "onLineFlag");
            return (Criteria) this;
        }

        public Criteria andOnLineFlagGreaterThanOrEqualTo(Byte value) {
            addCriterion("on_line_flag >=", value, "onLineFlag");
            return (Criteria) this;
        }

        public Criteria andOnLineFlagLessThan(Byte value) {
            addCriterion("on_line_flag <", value, "onLineFlag");
            return (Criteria) this;
        }

        public Criteria andOnLineFlagLessThanOrEqualTo(Byte value) {
            addCriterion("on_line_flag <=", value, "onLineFlag");
            return (Criteria) this;
        }

        public Criteria andOnLineFlagIn(List<Byte> values) {
            addCriterion("on_line_flag in", values, "onLineFlag");
            return (Criteria) this;
        }

        public Criteria andOnLineFlagNotIn(List<Byte> values) {
            addCriterion("on_line_flag not in", values, "onLineFlag");
            return (Criteria) this;
        }

        public Criteria andOnLineFlagBetween(Byte value1, Byte value2) {
            addCriterion("on_line_flag between", value1, value2, "onLineFlag");
            return (Criteria) this;
        }

        public Criteria andOnLineFlagNotBetween(Byte value1, Byte value2) {
            addCriterion("on_line_flag not between", value1, value2, "onLineFlag");
            return (Criteria) this;
        }

        public Criteria andCreateTimeIsNull() {
            addCriterion("create_time is null");
            return (Criteria) this;
        }

        public Criteria andCreateTimeIsNotNull() {
            addCriterion("create_time is not null");
            return (Criteria) this;
        }

        public Criteria andCreateTimeEqualTo(Date value) {
            addCriterion("create_time =", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeNotEqualTo(Date value) {
            addCriterion("create_time <>", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeGreaterThan(Date value) {
            addCriterion("create_time >", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeGreaterThanOrEqualTo(Date value) {
            addCriterion("create_time >=", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeLessThan(Date value) {
            addCriterion("create_time <", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeLessThanOrEqualTo(Date value) {
            addCriterion("create_time <=", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeIn(List<Date> values) {
            addCriterion("create_time in", values, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeNotIn(List<Date> values) {
            addCriterion("create_time not in", values, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeBetween(Date value1, Date value2) {
            addCriterion("create_time between", value1, value2, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeNotBetween(Date value1, Date value2) {
            addCriterion("create_time not between", value1, value2, "createTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeIsNull() {
            addCriterion("update_time is null");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeIsNotNull() {
            addCriterion("update_time is not null");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeEqualTo(Date value) {
            addCriterion("update_time =", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeNotEqualTo(Date value) {
            addCriterion("update_time <>", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeGreaterThan(Date value) {
            addCriterion("update_time >", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeGreaterThanOrEqualTo(Date value) {
            addCriterion("update_time >=", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeLessThan(Date value) {
            addCriterion("update_time <", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeLessThanOrEqualTo(Date value) {
            addCriterion("update_time <=", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeIn(List<Date> values) {
            addCriterion("update_time in", values, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeNotIn(List<Date> values) {
            addCriterion("update_time not in", values, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeBetween(Date value1, Date value2) {
            addCriterion("update_time between", value1, value2, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeNotBetween(Date value1, Date value2) {
            addCriterion("update_time not between", value1, value2, "updateTime");
            return (Criteria) this;
        }
    }

    public static class Criteria extends GeneratedCriteria {

        protected Criteria() {
            super();
        }
    }

    public static class Criterion {
        private String condition;

        private Object value;

        private Object secondValue;

        private boolean noValue;

        private boolean singleValue;

        private boolean betweenValue;

        private boolean listValue;

        private String typeHandler;

        public String getCondition() {
            return condition;
        }

        public Object getValue() {
            return value;
        }

        public Object getSecondValue() {
            return secondValue;
        }

        public boolean isNoValue() {
            return noValue;
        }

        public boolean isSingleValue() {
            return singleValue;
        }

        public boolean isBetweenValue() {
            return betweenValue;
        }

        public boolean isListValue() {
            return listValue;
        }

        public String getTypeHandler() {
            return typeHandler;
        }

        protected Criterion(String condition) {
            super();
            this.condition = condition;
            this.typeHandler = null;
            this.noValue = true;
        }

        protected Criterion(String condition, Object value, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.typeHandler = typeHandler;
            if (value instanceof List<?>) {
                this.listValue = true;
            } else {
                this.singleValue = true;
            }
        }

        protected Criterion(String condition, Object value) {
            this(condition, value, null);
        }

        protected Criterion(String condition, Object value, Object secondValue, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.secondValue = secondValue;
            this.typeHandler = typeHandler;
            this.betweenValue = true;
        }

        protected Criterion(String condition, Object value, Object secondValue) {
            this(condition, value, secondValue, null);
        }
    }
}