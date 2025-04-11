package com.bubusyaka.demo.service;

import com.bubusyaka.demo.repository.jpa.ProviderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProviderService {

    private final ProviderRepository providerRepository;

}
