// Supabase client
import { createClient } from '@supabase/supabase-js'

const supabaseUrl = 'https://cfzhvnijayxycovjnyay.supabase.co'
const supabaseKey = 'eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6ImNmemh2bmlqYXl4eWNvdmpueWF5Iiwicm9sZSI6ImFub24iLCJpYXQiOjE3MjUwMTk3MzMsImV4cCI6MjA0MDU5NTczM30.HLu-W-LIdcydCZEdHC7Kl9Uin21hKYKcQSJYbAnmdTA'
const supabase = createClient(supabaseUrl, supabaseKey)


// Ensure that you are correctly authenticating the user.
const { data: session } = await supabase.auth.getSession();

export default supabase;