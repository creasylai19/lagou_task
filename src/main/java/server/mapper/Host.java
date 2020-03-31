package server.mapper;

import java.util.concurrent.ConcurrentHashMap;

/**
 * 
 */
public class Host {

    private String name;
    private String appBase;
    private ConcurrentHashMap<String, Context> contexts = new ConcurrentHashMap<>();

    public Context addContext(Context context){
        return contexts.computeIfAbsent( context.getDocBase(), key->context );
    }

    public Context removeContext(Context context){
        return contexts.remove(context.getDocBase());
    }

    public Context getContext(String docBase) {
        return contexts.get(docBase);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAppBase() {
        return appBase;
    }

    public void setAppBase(String appBase) {
        this.appBase = appBase;
    }

}
