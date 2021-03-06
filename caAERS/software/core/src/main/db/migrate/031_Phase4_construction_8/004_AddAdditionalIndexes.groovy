class CreateIndexes extends edu.northwestern.bioinformatics.bering.Migration {
    void up() {
        execute('create index idx_ae_aereport_id on adverse_events(report_id)')
        execute('create index "idx_ae_terms_ae_id" on ae_terms (adverse_event_id)')

        execute('create index "idx_ctc_terms_cat_term" on ctc_terms(category_id, term)')

        execute('create index "idx_ctc_categories_ctc" on ctc_categories(version_id)')
        execute('create index "idx_ae_attr_ae_id" on ae_attributions (adverse_event_id)')

        execute('create index "idx_dishis_stud_dis_rp_id" on disease_histories(report_id, study_disease_id)')

        execute('create index "idx_med_dvcs_rp_id"  on ae_medical_devices(report_id)')
        execute('create index "idx_bio_inv_rp_id"  on ae_biological_interventions(report_id)')
        execute('create index "idx_bhavrl_inv_rp_id"  on ae_behavioral_interventions(report_id)')
        execute('create index "idx_rp_people_rp_id" on ae_report_people(report_id)')
        execute('create index "idx_rp_des_rp_id" on ae_report_descriptions(report_id)')
        execute('create index "idx_rad_inv_rp_id" on ae_radiation_interventions(report_id)')
        execute('create index "idx_srg_inv_rp_id" on ae_surgery_interventions(report_id)')
        execute('create index "idx_pr_thrpy_rp_id" on ae_prior_therapies(report_id)')
        execute('create index "idx_add_info_rp_id" on additional_information(report_id)')
        execute('create index "idx_con_med_rp_id" on concomitant_medications(report_id)')
        execute('create index "idx_labs_rp_id" on ae_labs(report_id)')
        execute('create index "idx_othr_cause_rp_id" on other_causes(report_id)')
        execute('create index "idx_parhis_rp_id" on participant_histories(report_id)')
        execute('create index "idx_pre_ex_cond_rp_id" on ae_pre_existing_conds(report_id)')
        execute('create index "idx_rpshdul_rp_id" on report_schedules(report_id)')
        execute('create index "idx_trtmts_rp_id" on treatments(report_id)')
        execute('create index "idx_rp_delv_rp_id" on report_deliveries (rp_id)')
        execute('create index "idx_shdl_nf_rp_id" on scheduled_notifications(rpsh_id)')
        execute('create index "idx_wf_comments_rp_id" on wf_review_comments(report_id)')

        execute('create index "idx_idnt_stu" on identifiers(stu_id)')
        execute('create index "idx_idnt_par" on identifiers(participant_id)')
        execute('create index "idx_ta_study_id" on treatment_assignment(study_id)')
        execute('create index "idx_sa_study_id" on study_agents(study_id)')
        execute('create index "idx_sa_inds_sa_id" on study_agent_inds(study_agent_id)')
    }
    void down(){
        execute('drop index idx_ae_aereport_id')
        execute('drop index "idx_ae_terms_ae_id"')
        execute('drop index "idx_ctc_terms_cat_term"')
        execute('drop index "idx_ctc_categories_ctc"')
        execute('drop index "idx_ae_attr_ae_id"')
        execute('drop index "idx_dishis_stud_dis_rp_id"')
        execute('drop index "idx_med_dvcs_rp_id"')
        execute('drop index "idx_rp_people_rp_id"')
        execute('drop index "idx_rp_des_rp_id"')
        execute('drop index "idx_rad_inv_rp_id"')
        execute('drop index "idx_srg_inv_rp_id"')
        execute('drop index "idx_pr_thrpy_rp_id"')
        execute('drop index "idx_add_info_rp_id"')
        execute('drop index "idx_con_med_rp_id"')
        execute('drop index "idx_labs_rp_id"')
        execute('drop index "idx_othr_cause_rp_id"')
        execute('drop index "idx_parhis_rp_id"')
        execute('drop index "idx_pre_ex_cond_rp_id"')
        execute('drop index "idx_rpshdul_rp_id"')
        execute('drop index "idx_trtmts_rp_id"')
        execute('drop index "idx_rp_delv_rp_id"')
        execute('drop index "idx_shdl_nf_rp_id"')
        execute('drop index "idx_wf_comments_rp_id"')
        execute('drop index "idx_idnt_stu"')
        execute('drop index "idx_idnt_par"')
        execute('drop index "idx_ta_study_id"')
        execute('drop index "idx_sa_study_id"')
        execute('drop index "idx_sa_inds_sa_id"')
        execute('drop index "idx_bio_inv_rp_id"')
        execute('drop index "idx_bhavrl_inv_rp_id"')
    }
}