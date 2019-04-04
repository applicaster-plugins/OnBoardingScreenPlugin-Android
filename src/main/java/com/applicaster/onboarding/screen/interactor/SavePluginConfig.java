package com.applicaster.onboarding.screen.interactor;

import com.applicaster.onboarding.screen.PluginRepository;
import com.applicaster.onboarding.screen.mapper.PluginDataMapper;

import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.observers.DisposableObserver;

public class SavePluginConfig implements UseCase<Boolean, SavePluginConfig.Params> {

    private final PluginDataMapper pluginDataMapper;
    private final PluginRepository pluginRepository;

    public SavePluginConfig(PluginRepository pluginRepository, PluginDataMapper pluginDataMapper) {
        this.pluginRepository = pluginRepository;
        this.pluginDataMapper = pluginDataMapper;
    }

    @Override public Boolean execute(Params params) {
        return pluginRepository.setPluginConfiguration(pluginDataMapper.mapParamsToConfig(params.pluginParams));
    }

    public static final class Params {
        private final Map pluginParams;

        private Params(Map pluginParams) {
            this.pluginParams = pluginParams;
        }

        public static Params forPluginParams(Map pluginParams) {
            return new Params(pluginParams);
        }
    }
}