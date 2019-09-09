package com.ontotext.trree.plugin.logger;

import com.ontotext.trree.sdk.Entities.Scope;
import com.ontotext.trree.sdk.*;
import org.eclipse.rdf4j.model.ValueFactory;
import org.eclipse.rdf4j.model.impl.SimpleValueFactory;

public class NotificationsLoggingPlugin extends PluginBase implements UpdateInterpreter, StatelessPlugin, StatementListener, PluginTransactionListener {

	public static final String NAMESPACE = "http://www.ontotext.com/notifications-logger#";

	boolean logEvents = false;
	private long idEnable;
	private long idDisable;

	@Override
	public String getName() {
		return "notifications-logger";
	}
	
	@Override
	public void initialize(InitReason reason, PluginConnection pluginConnection) {
		ValueFactory vf = SimpleValueFactory.getInstance();

		idEnable = pluginConnection.getEntities().put(vf.createIRI(NAMESPACE, "enable"), Scope.SYSTEM);
		idDisable = pluginConnection.getEntities().put(vf.createIRI(NAMESPACE, "disable"), Scope.SYSTEM);
	}

	@Override
	public boolean statementAdded(long subject, long predicate, long object, long context, boolean explicit,
								  PluginConnection pluginConnection) {
		if (logEvents && getLogger() != null)
			getLogger().info("statementAdded()[{}:{}\t{}:{}\t{}:{}\t{}:{}\ttranasaction:{}\texp:{}", 
					pluginConnection.getEntities().get(subject),subject,
					pluginConnection.getEntities().get(predicate),predicate,
					pluginConnection.getEntities().get(object),object,
					context != 0?pluginConnection.getEntities().get(context):"n/a", context,
							pluginConnection.getTransactionId(), explicit);
		return false;
	}

	@Override
	public boolean statementRemoved(long subject, long predicate, long object, long context, boolean explicit,
									PluginConnection pluginConnection) {
		if (logEvents && getLogger() != null)
			getLogger().info("statementRemoved()[{}:{}\t{}:{}\t{}:{}\t{}:{}\ttranasaction:{}\texp:{}", 
					pluginConnection.getEntities().get(subject),subject,
					pluginConnection.getEntities().get(predicate),predicate,
					pluginConnection.getEntities().get(object),object,
					context != 0?pluginConnection.getEntities().get(context):"n/a", context,
							pluginConnection.getTransactionId(), explicit);
		return false;
	}

	@Override
	public void transactionStarted(PluginConnection pluginConnection) {
		if (logEvents && getLogger() != null)
			getLogger().info("transactionStarted():{}", pluginConnection.getTransactionId());
	}

	@Override
	public void transactionCompleted(PluginConnection pluginConnection) {
		if (logEvents && getLogger() != null)
			getLogger().info("transactionCompleted():{}", pluginConnection.getTransactionId());
	}

	@Override
	public void transactionAborted(PluginConnection pluginConnection) {
		if (logEvents && getLogger() != null)
			getLogger().info("transactionAborted():{}", pluginConnection.getTransactionId());
	}

	@Override
	public void transactionCommit(PluginConnection pluginConnection) {
		if (logEvents && getLogger() != null)
			getLogger().info("transactionCommit():{}", pluginConnection.getTransactionId());
	}

	@Override
	public long[] getPredicatesToListenFor() {
		return new long[] {idEnable, idDisable};
	}

	@Override
	public boolean interpretUpdate(long subject, long predicate, long object, long context, boolean isAddition,
			boolean isExplicit, PluginConnection pluginConnection) {
		if (predicate == idEnable) {
			logEvents = true;
			return true;
		}
		if (predicate == idDisable) {
			logEvents = false;
			return true;
		}
		return false;
	}

}
