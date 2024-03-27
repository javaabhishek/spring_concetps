package com.study.axon.cqrs.productservice.command.interceptors;

import com.study.axon.cqrs.productservice.command.CreateProductCommand;
import com.study.axon.cqrs.productservice.core.data.ProductLookupEntity;
import com.study.axon.cqrs.productservice.core.data.ProductLookupRepository;
import org.axonframework.commandhandling.CommandMessage;
import org.axonframework.messaging.MessageDispatchInterceptor;
import org.springframework.stereotype.Component;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.annotation.Nonnull;
import java.util.List;
import java.util.function.BiFunction;

@Component
public class CreateProductCommandInterceptor implements MessageDispatchInterceptor<CommandMessage<?>> {

    private static final Logger LOGGER = LoggerFactory.getLogger(CreateProductCommandInterceptor.class);
    private final ProductLookupRepository productLookupRepository;

    public CreateProductCommandInterceptor(ProductLookupRepository productLookupRepository) {
        this.productLookupRepository = productLookupRepository;
    }

    @Nonnull
    @Override
    public BiFunction<Integer, CommandMessage<?>, CommandMessage<?>>
    handle(@Nonnull List<? extends CommandMessage<?>> messages) {


        BiFunction<Integer, CommandMessage<?>, CommandMessage<?>> retFun=(index,command)->{

            if(CreateProductCommand.class.equals(command.getPayloadType())) {
                CreateProductCommand createProductCommand = (CreateProductCommand)command.getPayload();

                ProductLookupEntity productLookupEntity =  productLookupRepository
                        .findByProductIdOrTitle(createProductCommand.getProductId(),
                        createProductCommand.getTitle());

                if(productLookupEntity != null) {
                    throw new IllegalStateException(
                            String.format("Product with productId %s or title %s already exist",
                                    createProductCommand.getProductId(), createProductCommand.getTitle())
                    );
                }
            }
            return command;
        };
        return retFun;
    }
}
