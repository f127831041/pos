package com.soho.pos.dao;


import com.soho.pos.dao.impl.BaseDAOImpl;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.JpaRepositoryFactory;
import org.springframework.data.jpa.repository.support.JpaRepositoryFactoryBean;
import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;
import org.springframework.data.repository.core.RepositoryInformation;
import org.springframework.data.repository.core.RepositoryMetadata;
import org.springframework.data.repository.core.support.RepositoryFactorySupport;
import org.springframework.util.Assert;

import javax.persistence.EntityManager;
import java.io.Serializable;

public class BaseDAOFactoryBean<T extends JpaRepository<S, ID>, S, ID extends Serializable> extends JpaRepositoryFactoryBean<T, S, ID> {
    public BaseDAOFactoryBean(Class<? extends T> repositoryInterface) {
        super(repositoryInterface);
    }
    @Override
    protected RepositoryFactorySupport createRepositoryFactory(EntityManager entityManager) {
        return new BaseRepositoryFactory(entityManager);
    }
    
    /**
     * 內部工廠模式
     */
    private static class BaseRepositoryFactory extends JpaRepositoryFactory {
    
        public BaseRepositoryFactory(EntityManager entityManager) {
            super(entityManager);
        }
    
        /**
         * 通過反射定義目標的Repository
         * @param information will never be {@literal null}.
         * @param entityManager will never be {@literal null}.
         * @return
         */
        @Override
        protected JpaRepositoryImplementation<?, ?> getTargetRepository(RepositoryInformation information, EntityManager entityManager) {
            JpaEntityInformation<?, Serializable> entityInformation = this.getEntityInformation(information.getDomainType());
            Object repository = this.getTargetRepositoryViaReflection(information, entityInformation, entityManager);
            Assert.isInstanceOf(BaseDAOImpl.class, repository);
            return (JpaRepositoryImplementation<?, ?>) repository;
        }
    
        /**
         * 設置實現類class
         * @param metadata
         * @return
         */
        @Override
        protected Class<?> getRepositoryBaseClass(RepositoryMetadata metadata) {
            return BaseDAOImpl.class;
        }
    }
}
