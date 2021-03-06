package org.projectodd.nodyn;

import java.lang.management.ManagementFactory;
import java.util.ArrayList;
import java.util.Map;

import org.dynjs.runtime.AbstractNativeFunction;
import org.dynjs.runtime.DynObject;
import org.dynjs.runtime.ExecutionContext;
import org.dynjs.runtime.GlobalObject;
import org.dynjs.runtime.PropertyDescriptor;
import org.projectodd.nodyn.bindings.Binding;

/**
 * A <code>Process</code> is a node.js application.
 * 
 * @author Bob McWhirter
 * @author Lance Ball
 */
public class Process extends DynObject {

	public Process(GlobalObject globalObject, String[] args) {
	    super(globalObject);

        setProperty("argv", args);
        setProperty("version", Node.VERSION );
		setProperty("versions", new Versions(globalObject) );
		setProperty("env", getProcessEnv(globalObject));
		setProperty("pid", ManagementFactory.getRuntimeMXBean().getName() );
		setProperty("execPath",  System.getProperty("user.dir")); // TODO: This doesn't make much sense

		setProperty("moduleLoadList", new ArrayList<String>() );
		setProperty("execArgv", null );
        
		setProperty("features", null );
		setProperty("_eval", null );
		setProperty("_print_eval", null );
		setProperty("_forceRepl", null );
		setProperty("execPath", null );
        setProperty("debugPort", null );
		
		setProperty("_needTickCallback", null );
		setProperty("reallyExit", null );
		setProperty("abort", null );
		setProperty("chdir", null );
		setProperty("umask", null );
		setProperty("getuid", null );
		setProperty("setuid", null );
		setProperty("getgid", null );
		setProperty("setgid", null );
		setProperty("_kill", null );
		setProperty("_debugProcess", null );
		setProperty("_debugPause", null );
		setProperty("_debugEnd", null );
		setProperty("hrtime", null );
		setProperty("dlopen", null );
		setProperty("uptime", null );
		//setProperty("uvCounters", null );
		setProperty("binding", new Binding(globalObject));
        setProperty("cwd", new AbstractNativeFunction(globalObject) {
            @Override
            public Object call(ExecutionContext context, Object self, Object... args) {
                return System.getProperty("user.dir");
            }
        });
	}
	
    private DynObject getProcessEnv(GlobalObject globalObject) {
        DynObject env = new DynObject(globalObject);
        String tmpDir = System.getProperty("java.io.tmpdir");
        if (tmpDir == null) {
            tmpDir = "/tmp";
        }
        Map<String,String> systemEnv = System.getenv();
        for(String key : systemEnv.keySet()) {
            String value = systemEnv.get(key);
            env.put(null, key.replaceAll("[\\./]", "_"), value, false);
        }
        env.put(null, "TMPDIR", tmpDir, false);
        env.put(null, "TMP", tmpDir, false);
        env.put(null, "TEMP", tmpDir, false);
        
        return env;
    }

    protected void setProperty(String name, final Object value) {
        this.defineOwnProperty(null, name, new PropertyDescriptor() {
            {
                set("Value", value );
                set("Writable", false);
                set("Enumerable", false);
                set("Configurable", false);
            }
        }, false);
    }
}
