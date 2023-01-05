package com.mybatis;

import com.mybatis.parser.GenericTokenParser;
import com.mybatis.parser.ParameterMapping;
import com.mybatis.parser.ParameterMappingTokenHandler;
import com.mybatis.typeHandler.IntegerTypeHandler;
import com.mybatis.typeHandler.StringTypeHandler;
import com.mybatis.typeHandler.TypeHandler;
import com.myc.User;

import java.lang.reflect.*;
import java.sql.*;
import java.util.*;

public class MapperProxyFactory {
    private static Map<Class, TypeHandler> typeHandlerMap=new HashMap<>();
    static {
        typeHandlerMap.put(Integer.class, new IntegerTypeHandler());
        typeHandlerMap.put(String.class,new StringTypeHandler());
    }
    public static <T> T getMapper(Class<T> mapper){
        Object proxyInstance = Proxy.newProxyInstance(ClassLoader.getSystemClassLoader(), new Class[]{mapper}, new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                //解析sql-->执行sql-->处理结果
                //获取数据库连接
                Connection connection=getConnection();

                //获取SQL
                Select selectSQL = method.getAnnotation(Select.class);
                String sql=selectSQL.value();

                //解析参数
                Map<String,Object> paramValueMap=new HashMap<>();
                Parameter[] parameters = method.getParameters();
                for (int i=0;i<parameters.length;i++){
                    Parameter parameter=parameters[i];
                    String name=parameter.getAnnotation(Param.class).value();
                    paramValueMap.put(name,args[i]);
                    paramValueMap.put(parameter.getName(),args[i]);
                }

                //解析sql
                ParameterMappingTokenHandler tokenHandler=new ParameterMappingTokenHandler();
                GenericTokenParser parser=new GenericTokenParser("#{","}",tokenHandler);
                String parserSQL=parser.parse(sql);
                List<ParameterMapping> parameterMappings=tokenHandler.getParameterMappings();

                //构造preparedStatement
                PreparedStatement statement=connection.prepareStatement(parserSQL);
                for (int i=0;i<parameterMappings.size();i++){
                    String property=parameterMappings.get(i).getProperty();
                    Object value=paramValueMap.get(property);
                    Class<?> type=value.getClass();
                    typeHandlerMap.get(type).setParameter(statement,i+1,value);
                }

                //执行preparedStatement
                statement.execute();

                //获取查询到的结果集
                ResultSet resultSet=statement.getResultSet();
                //获取查询到的字段名
                ResultSetMetaData metaData = resultSet.getMetaData();
                List<String> columnList=new ArrayList<>();
                for (int i = 0; i < metaData.getColumnCount(); i++) {
                    columnList.add(metaData.getColumnName(i+1));
                }

                //获取当前执行的方法希望的返回类型
                Class resultType=null;
                boolean isClass=true;
                Type genericReturnType=method.getGenericReturnType();
                if (genericReturnType instanceof Class){
                    resultType=(Class) genericReturnType;
                }else if (genericReturnType instanceof ParameterizedType){
                    Type[] actualTypeArguments=((ParameterizedType) genericReturnType).getActualTypeArguments();
                    resultType= (Class) actualTypeArguments[0];
                    isClass=false;
                }

                //将查询到的字段值封装为当前执行的方法希望的返回类型
                //获取封装用的setter方法
                Map<String,Method> setterMethodMap=new HashMap<>();
                for (Method m:resultType.getDeclaredMethods()){
                    if (m.getName().startsWith("set")){
                        String propertyName=m.getName().substring(3);
                        propertyName=propertyName.substring(0,1).toLowerCase(Locale.ROOT)+propertyName.substring(1);
                        setterMethodMap.put(propertyName,m);
                    }
                }

                List<Object> list=new ArrayList<>();
                while (resultSet.next()){
                    //一行记录-->Java类型
                    Object instance=resultType.newInstance();
                    for (String columnName:columnList){
                        Method setter=setterMethodMap.get(columnName);
                        Class clazz=setter.getParameterTypes()[0];
                        setter.invoke(instance,typeHandlerMap.get(clazz).getResult(resultSet,columnName));
                    }
                    list.add(instance);
                }

                connection.close();

                if (isClass){
                    return list.get(0);
                }
                return list;
            }


        });
        return (T) proxyInstance;
    }

    private static Connection getConnection() throws SQLException {
        String url="jdbc:mysql://localhost:3306/mybatisdemo?useSSL=false&serverTimezone=UTC";
        Connection connection= DriverManager.getConnection(url,"root","myc123");
        return connection;
    }
}
