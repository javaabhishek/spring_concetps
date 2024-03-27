package com.study.axon.cqrs.productservice.core.errorhandling;

import org.axonframework.eventhandling.EventMessage;
import org.axonframework.eventhandling.EventMessageHandler;
import org.axonframework.eventhandling.ListenerInvocationErrorHandler;

import javax.annotation.Nonnull;

public class ProductsServiceEventsErrorHandler implements ListenerInvocationErrorHandler {

	@Override
	public void onError(@Nonnull Exception exception, @Nonnull EventMessage<?> event, @Nonnull EventMessageHandler eventHandler) throws Exception {

		throw exception;//re-throwing error, i.e propagating error to controller/ProductsServiceErrorHandler.
	}
}