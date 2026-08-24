package com.example.fundforgoals.supabase

import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest

val supabase = createSupabaseClient(
    supabaseUrl = "https://xvzcfpdzrezeaftxlqvh.supabase.co",
    supabaseKey = "sb_publishable_mzrTz-gFWhKF5mnNN8G8XQ_6bqP_jn0"
) {
    install(Postgrest)
}