package server.mapper;

import java.util.concurrent.ConcurrentHashMap;

public class Context {

    private String docBase;
    private String path;
    private ConcurrentHashMap<String, Wrapper> wrappers = new ConcurrentHashMap<>();

    public Wrapper addWrapper(Wrapper wrapper){
        return wrappers.computeIfAbsent( wrapper.getUrlPattern(), key->wrapper );
    }

    public Wrapper removeWrapper(Wrapper wrapper){
        return wrappers.remove(wrapper.getUrlPattern());
    }

    public Wrapper getWrapper(String urlPattern) {
        return wrappers.get(urlPattern);
    }

    public String getDocBase() {
        return docBase;
    }

    public void setDocBase(String docBase) {
        this.docBase = docBase;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
