# This is the capistrano file for the GDOC application
# Usage cap [command] SERVER=[env] 
# server is optional, will defalut to dev
load 'deploy'

default_run_options[:pty] = true

set :application, "gdoc"
set :deploy_to, "/local"

if ENV["SERVER"] && ENV["SERVER"] == "prod"
  set :primary_server, "production.com"
  set :user, "ben"
elsif !ENV["SERVER"] || ENV["SERVER"] == "dev"
  set :primary_server, "141.161.30.205"
  set :user, "ben"
end

server primary_server, :app, :web, :db, :primary => true

set :scm, :none
set :deploy_via, :copy
set :repository do 
  fetch(:deploy_from)
end

set :war, "gdoc.war"
# TOMCAT SERVERS
set :jboss_home, "/local/jboss-4.2.3.GA"
set :jboss_start, "/local/jboss-4.2.3.GA/bin/startJboss.sh"
set :jboss_stop, "/local/jboss-4.2.3.GA/bin/stopJboss.sh"

# USER / SHELL
set :user, "acs224" # the user to run remote commands as
set :use_sudo, false

set :deploy_from do 
  dir = "/tmp/prep_#{release_name}"
  system("mkdir -p #{dir}")
  dir
end

# this is capistrano's default location.
# depending on the permissions of the server
# you may need to create it and chown it over
# to :user (e.g. chown -R robotuser:robotuser /u)
set :deploy_to do 
  "/local/#{application}"
end

#
# simple interactions with the JBoss server
#
namespace :jboss do

  desc "start JBoss"
  task :start do
    sudo "#{jboss_start}"
  end

  desc "stop JBoss"
  task :stop do
    sudo "#{jboss_stop}"
  end

  desc "stop and start JBoss"
  task :restart do
    jboss.stop rescue nil
    sleep 20
    jboss.start
  end
  
  desc "tail JBoss server log"
  task :tail do
    stream "tail -f #{jboss_home}/server/default/log/server.log"
  end

end

#
# link the current/whatever.war into our webapps/whatever.war
#
after 'deploy:setup' do
  cmd = "ln -s #{deploy_to}/current/#{war} #{jboss_home}/server/default/deploy/#{war}"
  puts cmd
  sudo cmd
end

# collect up our war into the deploy_from folder
# notice that all we're doing is a copy here,
# so it is pretty easy to swap this out for
# a wget command, which makes sense if you're
# using a continuous integration server like
# bamboo. (more on this later).
before 'deploy:update_code' do
  unless(war.nil?)
    puts "get war"
    system("cp #{war} #{deploy_from}")
    puts system("ls -l #{deploy_from}")
  end
end

# restart JBoss
namespace :deploy do
  task :restart do
    jboss.restart
  end
end

#
# Disable all the default tasks that
# either don't apply, or I haven't made work.
#
namespace :deploy do
  [ :upload, :cold, :start, :stop, :migrate, :migrations ].each do |default_task|
    desc "[internal] disabled"
    task default_task do
      # disabled
    end
  end

  namespace :web do
    [ :disable, :enable ].each do |default_task|
      desc "[internal] disabled"
      task default_task do
        # disabled
      end
    end
  end

  namespace :pending do
    [ :default, :diff ].each do |default_task|
      desc "[internal] disabled"
      task default_task do
        # disabled
      end
    end
  end
end
