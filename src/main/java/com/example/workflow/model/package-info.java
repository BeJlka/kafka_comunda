@GenericGenerator(name = "UUID_GENERATOR", strategy = "uuid2",
        parameters = {
                @Parameter(name = "sequence_name", value = "UUID_SEQUENCE")
        }
)
package com.example.workflow.model;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;