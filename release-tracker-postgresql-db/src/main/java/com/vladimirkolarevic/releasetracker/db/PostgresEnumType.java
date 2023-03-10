package com.vladimirkolarevic.releasetracker.db;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.type.EnumType;


public class PostgresEnumType extends EnumType<ReleaseStatusJpaEntity> {

    @Override
    public void nullSafeSet(PreparedStatement ps, ReleaseStatusJpaEntity obj, int index,
                            SharedSessionContractImplementor session) throws HibernateException, SQLException {
        if (obj == null) {
            ps.setNull(index, Types.OTHER);
        } else {
            ps.setObject(index, obj.toString(), Types.OTHER);
        }
    }


}
