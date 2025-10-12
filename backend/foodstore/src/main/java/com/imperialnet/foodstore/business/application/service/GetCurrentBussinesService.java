package com.imperialnet.foodstore.business.application.service;

import com.imperialnet.foodstore.business.application.port.in.GetCurrentBusinessUseCase;
import com.imperialnet.foodstore.business.application.port.out.BusinessHourRepositoryPort;
import com.imperialnet.foodstore.business.application.port.out.BusinessRepositoryPort;
import com.imperialnet.foodstore.business.domain.model.Business;
import com.imperialnet.foodstore.products.domain.model.Product;
import com.imperialnet.foodstore.publicapi.application.ports.in.GetActiveProductsUseCase;
import com.imperialnet.foodstore.users.domain.exception.BusinessException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GetCurrentBussinesService implements GetCurrentBusinessUseCase {

    private final BusinessRepositoryPort businessRepositoryPort;

    public GetCurrentBussinesService(BusinessRepositoryPort businessRepositoryPort) {
        this.businessRepositoryPort = businessRepositoryPort;
    }


    @Override
    public Business getCurrentBusiness() {
        Business currentBusiness=null;
        // traer todos los business, buscar el primero que este actuvo y devolverlo o lanzar excepcion
        List<Business> businessList= businessRepositoryPort.findAll();
        for (Business business: businessList) {
            if(business.isActive()){
                currentBusiness=business;
                break;
            }
        }
        if(currentBusiness==null){
            throw new BusinessException("No hay ningún negocio activo");
    }
        return currentBusiness;
    }



}
