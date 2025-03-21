package com.dxdou.snowai.handler;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedTypes;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 向量类型处理器
 *
 * @author foreverdxdou
 */
@MappedTypes(float[].class)
public class VectorTypeHandler extends BaseTypeHandler<float[]> {

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, float[] parameter, JdbcType jdbcType)
            throws SQLException {
        // 将float数组转换为PostgreSQL向量格式
        StringBuilder vectorStr = new StringBuilder("[");
        for (int j = 0; j < parameter.length; j++) {
            vectorStr.append(parameter[j]);
            if (j < parameter.length - 1) {
                vectorStr.append(",");
            }
        }
        vectorStr.append("]");
        ps.setString(i, vectorStr.toString());
    }

    @Override
    public float[] getNullableResult(ResultSet rs, String columnName) throws SQLException {
        return parseVector(rs.getString(columnName));
    }

    @Override
    public float[] getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        return parseVector(rs.getString(columnIndex));
    }

    @Override
    public float[] getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        return parseVector(cs.getString(columnIndex));
    }

    /**
     * 解析向量字符串
     *
     * @param vectorStr 向量字符串
     * @return 向量数组
     */
    private float[] parseVector(String vectorStr) {
        if (vectorStr == null || vectorStr.isEmpty()) {
            return null;
        }

        // 移除方括号并分割
        String[] values = vectorStr.replaceAll("[\\[\\]]", "").split(",");
        float[] vector = new float[values.length];

        // 解析每个值
        for (int i = 0; i < values.length; i++) {
            vector[i] = Float.parseFloat(values[i].trim());
        }

        return vector;
    }
}